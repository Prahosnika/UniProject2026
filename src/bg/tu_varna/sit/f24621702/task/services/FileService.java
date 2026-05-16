package bg.tu_varna.sit.f24621702.task.services;

import bg.tu_varna.sit.f24621702.task.exceptions.*;
import bg.tu_varna.sit.f24621702.task.interfaces.StorageStrategy;
import bg.tu_varna.sit.f24621702.task.models.*;
import bg.tu_varna.sit.f24621702.task.observer.*;
import bg.tu_varna.sit.f24621702.task.services.impl.*;
import bg.tu_varna.sit.f24621702.task.states.*;
import bg.tu_varna.sit.f24621702.task.ui.ConsoleUI;
import bg.tu_varna.sit.f24621702.task.visitor.DeterministicVisitor;
import bg.tu_varna.sit.f24621702.task.xml.XmlManager;
import bg.tu_varna.sit.f24621702.task.regex.RegexParser;
import bg.tu_varna.sit.f24621702.task.regex.nodes.*;
import java.util.*;

public class FileService {
    private static FileService instance;
    private final String STORAGE_DIR = "db/";
    private final StorageStrategy storage = new XmlManager();
    private final TransformationService transformService = new TransformationService();
    private final AnalysisService analysisService = new AnalysisService();

    private String currentFilePath;
    private boolean isFileOpen = false;
    private List<Automaton> loadedAutomata = new ArrayList<>();
    private FileState state = new ClosedFileState();
    private final List<AutomatonObserver> observers = new ArrayList<>();

    private FileService() { addObserver(new AuditLogger()); }

    public static FileService getInstance() {
        if (instance == null) instance = new FileService();
        return instance;
    }

    // Делегиране на методи към специализирани услуги
    public void union(String id1, String id2) throws AutomatonNotFoundException {
        Automaton res = transformService.applyUnion(findById(id1), findById(id2));
        addAutomaton(res, "Union");
    }

    public void recognize(String id, String word) throws AutomatonNotFoundException {
        if (analysisService.recognize(findById(id), word))
            ConsoleUI.printSuccess("Word IS recognized.");
        else ConsoleUI.printInfo("Word is NOT recognized.");
    }

    public void concat(String id1, String id2) throws AutomatonNotFoundException {
        Automaton a1 = findById(id1);
        Automaton a2 = findById(id2);
        Automaton res = transformService.applyConcat(a1, a2);
        addAutomaton(res, "Concat of " + id1 + " and " + id2);
    }

    public void iteration(String id) throws AutomatonNotFoundException {
        Automaton a = findById(id);
        Automaton res = transformService.applyIteration(a);
        addAutomaton(res, "Iteration (un) of " + id);
    }

    public void checkEmpty(String id) throws AutomatonNotFoundException {
        if (analysisService.isLanguageEmpty(findById(id)))
            System.out.println("Language IS empty.");
        else System.out.println("Language is NOT empty.");
    }

    public void determinize(String id) throws AutomatonNotFoundException {
        Automaton dfa = analysisService.determinize(findById(id));
        addAutomaton(dfa, "Determinization");
    }

    public void checkDeterministic(String id) throws AutomatonNotFoundException {
        Automaton a = findById(id);
        bg.tu_varna.sit.f24621702.task.visitor.DeterministicVisitor v = new bg.tu_varna.sit.f24621702.task.visitor.DeterministicVisitor();
        a.accept(v);
        if (v.isDeterministic())
            bg.tu_varna.sit.f24621702.task.ui.ConsoleUI.printSuccess("Автоматът е ДЕТЕРМИНИРАН.");
        else
            bg.tu_varna.sit.f24621702.task.ui.ConsoleUI.printInfo("Автоматът е НЕДЕТЕРМИНИРАН.");
    }

    public void fromRegex(String regex) throws InvalidRegexException {
        try {
            RegexParser parser = new RegexParser();
            String postfix = parser.infixToPostfix(regex);
            Stack<RegexNode> nodeStack = new Stack<>();
            for (char c : postfix.toCharArray()) {
                if (Character.isLetterOrDigit(c)) nodeStack.push(new LiteralNode(String.valueOf(c)));
                else if (c == '*') nodeStack.push(new StarNode(nodeStack.pop()));
                else if (c == '|') { RegexNode r = nodeStack.pop(); RegexNode l = nodeStack.pop(); nodeStack.push(new UnionNode(l, r)); }
                else if (c == '.') { RegexNode r = nodeStack.pop(); RegexNode l = nodeStack.pop(); nodeStack.push(new ConcatNode(l, r)); }
            }
            addAutomaton(nodeStack.pop().transform(), "Regex: " + regex);
        } catch (Exception e) { throw new InvalidRegexException(regex); }
    }

    // Системни методи
    public void addAutomaton(Automaton a, String method) {
        loadedAutomata.add(a);
        for (AutomatonObserver o : observers) o.onAutomatonCreated(a.getId(), method);
    }

    public void saveSpecificAutomaton(String id, String fileName) throws AutomatonNotFoundException {
        Automaton a = findById(id);
        try {
            String fullPath = STORAGE_DIR + fileName;
            // Използваме стратегията за запис само на този автомат
            storage.save(fullPath, java.util.Collections.singletonList(a));
            bg.tu_varna.sit.f24621702.task.ui.ConsoleUI.printSuccess("Автомат " + id + " беше записан успешно в " + fullPath);
        } catch (Exception e) {
            bg.tu_varna.sit.f24621702.task.ui.ConsoleUI.printError("Грешка при експортиране: " + e.getMessage());
        }
    }

    private Automaton findById(String id) throws AutomatonNotFoundException {
        for (Automaton a : loadedAutomata) if (a.getId().equals(id)) return a;
        throw new AutomatonNotFoundException(id);
    }

    public void open(String f) { state.open(f); }
    public void performOpen(String f) {
        try { this.loadedAutomata = storage.load(STORAGE_DIR + f); this.currentFilePath = f; this.isFileOpen = true; setState(new OpenFileState()); }
        catch (Exception e) { ConsoleUI.printError(e.getMessage()); }
    }

    // Помощни методи за RegexNodes (трябва да са достъпни през инстанцията)
    public Automaton createBasicAutomaton(String s) { return transformService.createBasicAutomaton(s); }
    public Automaton applyIteration(Automaton a) { return transformService.applyIteration(a); }
    public Automaton applyConcat(Automaton a1, Automaton a2) { return transformService.applyConcat(a1, a2); }
    public Automaton applyUnion(Automaton a1, Automaton a2) { return transformService.applyUnion(a1, a2); }

    // Гетери и Сетъри
    public void setState(FileState s) { this.state = s; }
    public boolean isOpen() { return isFileOpen; }
    public void addObserver(AutomatonObserver o) { observers.add(o); }
    public void close() { loadedAutomata.clear(); isFileOpen = false; setState(new ClosedFileState()); }
    public void save() { saveAs(currentFilePath); }
    public void saveAs(String f) { try { storage.save(STORAGE_DIR + f, loadedAutomata); currentFilePath = f; } catch (Exception e) {}}
    public void listAutomata() { for (Automaton a : loadedAutomata) System.out.println("ID: " + a.getId()); }
    public void printAutomaton(String id) throws AutomatonNotFoundException { findById(id).printInfo(); }
}