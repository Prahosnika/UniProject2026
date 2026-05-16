package bg.tu_varna.sit.f24621702.task.services;

import bg.tu_varna.sit.f24621702.task.exceptions.*;
import bg.tu_varna.sit.f24621702.task.interfaces.StorageStrategy;
import bg.tu_varna.sit.f24621702.task.models.*;
import bg.tu_varna.sit.f24621702.task.observer.*;
import bg.tu_varna.sit.f24621702.task.services.impl.*;
import bg.tu_varna.sit.f24621702.task.states.*;
import bg.tu_varna.sit.f24621702.task.ui.ConsoleUI;
import bg.tu_varna.sit.f24621702.task.xml.XmlManager;
import bg.tu_varna.sit.f24621702.task.regex.RegexParser;
import bg.tu_varna.sit.f24621702.task.regex.nodes.*;
import java.util.*;

/**
 * Централен мениджър на приложението, реализиращ шаблона Facade.
 * Този клас е Singleton и служи като единствена точка за достъп до бизнес логиката.
 * Управлява състоянието на отворените файлове, списъка с автомати и делегира
 * изчисленията към специализирани услуги (Transformation и Analysis).
 *
 * @author f24621702
 * @version 1.5
 */
public class FileService {
    /** Единствената инстанция на класа (Singleton). */
    private static FileService instance;

    /** Път до директорията за съхранение на файлове. */
    private final String STORAGE_DIR = "db/";

    /** Стратегия за съхранение (Strategy Pattern), по подразбиране XML. */
    private final StorageStrategy storage = new XmlManager();

    /** Услуга за структурни трансформации на автомати. */
    private final TransformationService transformService = new TransformationService();

    /** Услуга за логически анализ и симулация на автомати. */
    private final AnalysisService analysisService = new AnalysisService();

    /** Път до текущо отворения файл. */
    private String currentFilePath;

    /** Флаг, индикиращ дали има зареден файл в паметта. */
    private boolean isFileOpen = false;

    /** Списък с всички заредени автомати в текущата сесия. */
    private List<Automaton> loadedAutomata = new ArrayList<>();

    /** Текущо състояние на файловия мениджър (State Pattern). */
    private FileState state = new ClosedFileState();

    /** Списък с наблюдатели за събития (Observer Pattern). */
    private final List<AutomatonObserver> observers = new ArrayList<>();

    /**
     * Частен конструктор. Регистрира AuditLogger като наблюдател по подразбиране.
     */
    private FileService() { addObserver(new AuditLogger()); }

    /**
     * Връща инстанцията на FileService.
     * @return Текущата инстанция.
     */
    public static FileService getInstance() {
        if (instance == null) instance = new FileService();
        return instance;
    }

    /**
     * Извършва обединение на два автомата.
     * @param id1 ID на първия автомат.
     * @param id2 ID на втория автомат.
     * @throws AutomatonNotFoundException Ако някой от автоматите не е зареден.
     */
    public void union(String id1, String id2) throws AutomatonNotFoundException {
        Automaton res = transformService.applyUnion(findById(id1), findById(id2));
        addAutomaton(res, "Union");
    }

    /**
     * Симулира работата на автомат с дадена дума.
     * @param id ID на автомата.
     * @param word Дума за проверка.
     * @throws AutomatonNotFoundException Ако автоматът не е намерен.
     */
    public void recognize(String id, String word) throws AutomatonNotFoundException {
        if (analysisService.recognize(findById(id), word))
            ConsoleUI.printSuccess("Думата '" + word + "' СЕ разпознава.");
        else ConsoleUI.printInfo("Думата '" + word + "' НЕ се разпознава.");
    }

    /**
     * Извършва конкатенация на два автомата.
     * @param id1 ID на първия автомат.
     * @param id2 ID на втория автомат.
     * @throws AutomatonNotFoundException При невалидно ID.
     */
    public void concat(String id1, String id2) throws AutomatonNotFoundException {
        Automaton res = transformService.applyConcat(findById(id1), findById(id2));
        addAutomaton(res, "Concat of " + id1 + " and " + id2);
    }

    /**
     * Прилага итерация върху автомат (Positive closure).
     * @param id ID на автомата.
     * @throws AutomatonNotFoundException При невалидно ID.
     */
    public void iteration(String id) throws AutomatonNotFoundException {
        Automaton res = transformService.applyIteration(findById(id));
        addAutomaton(res, "Iteration (un) of " + id);
    }

    /**
     * Проверява дали езикът на автомата е празен.
     * @param id ID на автомата.
     * @throws AutomatonNotFoundException При невалидно ID.
     */
    public void checkEmpty(String id) throws AutomatonNotFoundException {
        if (analysisService.isLanguageEmpty(findById(id)))
            System.out.println("Езикът е ПРАЗЕН.");
        else System.out.println("Езикът НЕ е празен.");
    }

    /**
     * Трансформира НКА в ДКА (Determinization).
     * @param id ID на изходния автомат.
     * @throws AutomatonNotFoundException При невалидно ID.
     */
    public void determinize(String id) throws AutomatonNotFoundException {
        Automaton dfa = analysisService.determinize(findById(id));
        addAutomaton(dfa, "Determinization");
    }

    /**
     * Анализира детерминираността на автомат чрез Visitor Pattern.
     * @param id ID на автомата.
     * @throws AutomatonNotFoundException При невалидно ID.
     */
    public void checkDeterministic(String id) throws AutomatonNotFoundException {
        Automaton a = findById(id);
        bg.tu_varna.sit.f24621702.task.visitor.DeterministicVisitor v = new bg.tu_varna.sit.f24621702.task.visitor.DeterministicVisitor();
        a.accept(v);
        if (v.isDeterministic())
            ConsoleUI.printSuccess("Автоматът е ДЕТЕРМИНИРАН.");
        else
            ConsoleUI.printInfo("Автоматът е НЕДЕТЕРМИНИРАН.");
    }

    /**
     * Създава автомат от регулярен израз чрез Thompson's construction.
     * Използва Composite Pattern за изграждане на дърво на израза.
     * @param regex Регулярен израз.
     * @throws InvalidRegexException При синтактична грешка в израза.
     */
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

    /**
     * Добавя нов автомат в паметта и известява всички регистрирани наблюдатели.
     * @param a Новият автомат.
     * @param method Описание на метода на създаване.
     */
    public void addAutomaton(Automaton a, String method) {
        loadedAutomata.add(a);
        for (AutomatonObserver o : observers) o.onAutomatonCreated(a.getId(), method);
    }

    /**
     * Експортира конкретен автомат в отделен файл.
     * @param id ID на автомата.
     * @param fileName Име на изходния файл.
     * @throws AutomatonNotFoundException При невалидно ID.
     */
    public void saveSpecificAutomaton(String id, String fileName) throws AutomatonNotFoundException {
        Automaton a = findById(id);
        try {
            String fullPath = STORAGE_DIR + fileName;
            storage.save(fullPath, java.util.Collections.singletonList(a));
            ConsoleUI.printSuccess("Автоматът беше записан успешно.");
        } catch (Exception e) {
            ConsoleUI.printError("Грешка при запис.");
        }
    }

    /** Вътрешен метод за търсене. @throws AutomatonNotFoundException */
    private Automaton findById(String id) throws AutomatonNotFoundException {
        for (Automaton a : loadedAutomata) if (a.getId().equals(id)) return a;
        throw new AutomatonNotFoundException(id);
    }

    /** Отваря файл спрямо текущото състояние. @param f Име на файл. */
    public void open(String f) { state.open(f); }

    /** Изпълнява реалното зареждане на данни. @param f Име на файл. */
    public void performOpen(String f) {
        try {
            this.loadedAutomata = storage.load(STORAGE_DIR + f);
            this.currentFilePath = f;
            this.isFileOpen = true;
            setState(new OpenFileState());
        } catch (Exception e) { ConsoleUI.printError("Грешка при отваряне."); }
    }

    /** Помощен метод за LiteralNode. @return Базов автомат. */
    public Automaton createBasicAutomaton(String s) { return transformService.createBasicAutomaton(s); }
    /** Помощен метод за StarNode. @return Итериран автомат. */
    public Automaton applyIteration(Automaton a) { return transformService.applyIteration(a); }
    /** Помощен метод за ConcatNode. @return Конкатениран автомат. */
    public Automaton applyConcat(Automaton a1, Automaton a2) { return transformService.applyConcat(a1, a2); }
    /** Помощен метод за UnionNode. @return Обединен автомат. */
    public Automaton applyUnion(Automaton a1, Automaton a2) { return transformService.applyUnion(a1, a2); }

    /** Променя състоянието на услугата. @param s Ново състояние. */
    public void setState(FileState s) { this.state = s; }
    /** Проверява дали има отворен файл. @return true ако е отворен. */
    public boolean isOpen() { return isFileOpen; }
    /** Регистрира нов наблюдател. @param o Наблюдател. */
    public void addObserver(AutomatonObserver o) { observers.add(o); }
    /** Затваря текущата сесия и изчиства паметта. */
    public void close() { loadedAutomata.clear(); isFileOpen = false; setState(new ClosedFileState()); ConsoleUI.printSuccess("Файлът е затворен."); }
    /** Записва промените в текущия файл. */
    public void save() { saveAs(currentFilePath); }
    /** Записва промените в нов файл. @param f Път. */
    public void saveAs(String f) { try { storage.save(STORAGE_DIR + f, loadedAutomata); currentFilePath = f; ConsoleUI.printSuccess("Записано."); } catch (Exception e) {}}
    /** Извежда списък на всички автомати. */
    public void listAutomata() { if(loadedAutomata.isEmpty()) ConsoleUI.printInfo("Няма автомати."); for (Automaton a : loadedAutomata) System.out.println("ID: " + a.getId()); }
    /** Принтира детайли за конкретен автомат. @param id ID. @throws AutomatonNotFoundException */
    public void printAutomaton(String id) throws AutomatonNotFoundException { findById(id).printInfo(); }
}