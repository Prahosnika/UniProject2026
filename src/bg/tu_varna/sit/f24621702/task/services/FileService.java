package bg.tu_varna.sit.f24621702.task.services;

import bg.tu_varna.sit.f24621702.task.exceptions.AutomatonNotFoundException;
import bg.tu_varna.sit.f24621702.task.exceptions.InvalidRegexException;
import bg.tu_varna.sit.f24621702.task.interfaces.StorageStrategy;
import bg.tu_varna.sit.f24621702.task.models.Automaton;
import bg.tu_varna.sit.f24621702.task.models.AutomatonBuilder;
import bg.tu_varna.sit.f24621702.task.models.Transition;
import bg.tu_varna.sit.f24621702.task.observer.AuditLogger;
import bg.tu_varna.sit.f24621702.task.observer.AutomatonObserver;
import bg.tu_varna.sit.f24621702.task.regex.RegexParser;
import bg.tu_varna.sit.f24621702.task.regex.nodes.*;
import bg.tu_varna.sit.f24621702.task.states.ClosedFileState;
import bg.tu_varna.sit.f24621702.task.states.FileState;
import bg.tu_varna.sit.f24621702.task.states.OpenFileState;
import bg.tu_varna.sit.f24621702.task.ui.ConsoleUI;
import bg.tu_varna.sit.f24621702.task.visitor.DeterministicVisitor;
import bg.tu_varna.sit.f24621702.task.xml.XmlManager;

import java.util.*;

public class FileService {
    private static FileService instance;
    private final String STORAGE_DIR = "db/";
    private final StorageStrategy storage = new XmlManager();
    private String currentFilePath;
    private boolean isFileOpen = false;
    private int stateCounter = 0;
    private List<Automaton> loadedAutomata = new ArrayList<>();

    // State & Observer полета
    private FileState state = new ClosedFileState();
    private final List<AutomatonObserver> observers = new ArrayList<>();

    private FileService() {
        addObserver(new AuditLogger());
    }

    public void setState(FileState newState) { this.state = newState; }
    public boolean isOpen() { return isFileOpen; }

    public static FileService getInstance() {
        if (instance == null) instance = new FileService();
        return instance;
    }
    public void addObserver(AutomatonObserver o) { observers.add(o); }

    private void notifyObservers(String id, String method) {
        for (AutomatonObserver o : observers) o.onAutomatonCreated(id, method);
    }

    public void addAutomaton(Automaton a, String method) {
        loadedAutomata.add(a);
        notifyObservers(a.getId(), method);
    }

    public void open(String fileName) { state.open(fileName); }
    public void performOpen(String fileName) {
        try {
            this.loadedAutomata = storage.load(STORAGE_DIR + fileName);
            this.currentFilePath = fileName;
            this.isFileOpen = true;
            setState(new OpenFileState());
            ConsoleUI.printSuccess("Успешно зареден: " + fileName);
        } catch (Exception e) {
            ConsoleUI.printError("Грешка при отваряне: " + e.getMessage());
        }
    }

    public void close() {
        loadedAutomata.clear();
        isFileOpen = false;
        currentFilePath = null;
        setState(new ClosedFileState());
        ConsoleUI.printSuccess("Файлът е затворен.");
    }

    public void save() {
        if (!isFileOpen) return;
        saveAs(currentFilePath);
    }

    public void saveAs(String fileName) {
        if (!isFileOpen) return;
        try {
            storage.save(STORAGE_DIR + fileName, loadedAutomata);
            this.currentFilePath = fileName;
            ConsoleUI.printSuccess("Записано в " + fileName);
        } catch (Exception e) {
            ConsoleUI.printError("Грешка при запис: " + e.getMessage());
        }
    }

    public void listAutomata() {
        if (loadedAutomata.isEmpty()) { ConsoleUI.printInfo("Списъкът е празен."); return; }
        for (Automaton a : loadedAutomata) System.out.println("ID: " + a.getId());
    }

    public void printAutomaton(String id) throws AutomatonNotFoundException {
        Automaton a = findById(id);
        a.printInfo();
    }

    public void saveSpecificAutomaton(String id, String fileName) throws AutomatonNotFoundException {
        Automaton a = findById(id);

        try {
            String fullPath = STORAGE_DIR + fileName;

            List<Automaton> singleList = Collections.singletonList(a);
            storage.save(fullPath, singleList);

            ConsoleUI.printSuccess("Автомат " + id + " беше записан успешно в " + fullPath);
        } catch (Exception e) {
            ConsoleUI.printError("Грешка при експортиране на автомат: " + e.getMessage());
        }
    }

    public void checkEmpty(String id) throws AutomatonNotFoundException {
        Automaton a = findById(id);

        // Алгоритъм за достигане (BFS/DFS) от начално състояние до крайните
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(a.getInitialState());
        visited.add(a.getInitialState());

        boolean reachable = false;
        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (a.getFinalStates().contains(current)) {
                reachable = true;
                break;
            }
            for (Transition t : a.getTransitions()) {
                if (t.getFromState().equals(current) && !visited.contains(t.getToState())) {
                    visited.add(t.getToState());
                    queue.add(t.getToState());
                }
            }
        }

        if (reachable) System.out.println("The language of automaton " + id + " is NOT empty.");
        else System.out.println("The language of automaton " + id + " IS empty.");
    }

    public void checkDeterministic(String id) throws AutomatonNotFoundException {
        Automaton a = findById(id);
        DeterministicVisitor v = new DeterministicVisitor();
        a.accept(v);
        if (v.isDeterministic()) ConsoleUI.printSuccess("Автоматът е ДЕТЕРМИНИРАН.");
        else ConsoleUI.printInfo("Автоматът е НЕДЕТЕРМИНИРАН.");
    }

    private Automaton findById(String id) throws AutomatonNotFoundException {
        for (Automaton a : loadedAutomata) {
            if (a.getId().equals(id)) return a;
        }
        throw new AutomatonNotFoundException(id);
    }

    public void recognize(String id, String word) throws AutomatonNotFoundException {
        Automaton a = findById(id);

        // 1. Първоначални състояния: ε-затваряне на началното състояние
        Set<String> currentStates = new HashSet<>();
        currentStates.add(a.getInitialState());
        currentStates = epsilonClosure(currentStates, a);

        // 2. Обработка на всеки символ от думата
        for (char symbol : word.toCharArray()) {
            String sym = String.valueOf(symbol);
            Set<String> nextStates = new HashSet<>();

            for (String state : currentStates) {
                for (Transition t : a.getTransitions()) {
                    if (t.getFromState().equals(state) && t.getSymbol().equals(sym)) {
                        nextStates.add(t.getToState());
                    }
                }
            }
            // След всеки символ отново правим ε-затваряне
            currentStates = epsilonClosure(nextStates, a);

            if (currentStates.isEmpty()) break;
        }

        // 3. Проверка дали някое от финалните състояния е достигнато
        boolean accepted = false;
        for (String state : currentStates) {
            if (a.getFinalStates().contains(state)) {
                accepted = true;
                break;
            }
        }

        if (accepted) System.out.println("The word '" + word + "' IS recognized by automaton " + id);
        else System.out.println("The word '" + word + "' is NOT recognized by automaton " + id);
    }

    // Помощен метод за ε-затваряне (намира всички достижими състояния чрез epsilon)
    private Set<String> epsilonClosure(Set<String> states, Automaton a) {
        Set<String> closure = new HashSet<>(states);
        Stack<String> stack = new Stack<>();
        stack.addAll(states);

        while (!stack.isEmpty()) {
            String current = stack.pop();
            for (Transition t : a.getTransitions()) {
                if (t.getFromState().equals(current) && isEpsilon(t.getSymbol())) {
                    if (!closure.contains(t.getToState())) {
                        closure.add(t.getToState());
                        stack.push(t.getToState());
                    }
                }
            }
        }
        return closure;
    }

    private boolean isEpsilon(String sym) {
        return sym.equalsIgnoreCase("epsilon") || sym.equals("ε") || sym.isEmpty();
    }

    public void union(String id1, String id2) throws AutomatonNotFoundException {
        Automaton a1 = findById(id1);
        Automaton a2 = findById(id2);
        Automaton res = applyUnion(a1, a2);
        addAutomaton(res, "Union of " + id1 + " and " + id2);
    }

    // Помощен метод за копиране и преименуване на състояния
    private void mergeAutomaton(Automaton target, Automaton source, String prefix) {
        for (Transition t : source.getTransitions()) {
            target.addTransition(new Transition(
                    prefix + t.getFromState(),
                    prefix + t.getToState(),
                    t.getSymbol()
            ));
            target.addState(prefix + t.getFromState());
            target.addState(prefix + t.getToState());
        }
    }

    public void concat(String id1, String id2) throws AutomatonNotFoundException {
        Automaton a1 = findById(id1);
        Automaton a2 = findById(id2);
        Automaton res = applyConcat(a1, a2);
        addAutomaton(res, "Concat of " + id1 + " and " + id2);
    }

    public void iteration(String id) throws AutomatonNotFoundException {
        Automaton a = findById(id);
        Automaton res = applyIteration(a); // ползваме вътрешния метод
        addAutomaton(res, "Iteration (un)");
    }

    public void fromRegex(String regex) throws InvalidRegexException {
        try {
            long openBrackets = regex.chars().filter(ch -> ch == '(').count();
            long closeBrackets = regex.chars().filter(ch -> ch == ')').count();
            if (openBrackets != closeBrackets) throw new Exception("Unbalanced brackets");

            RegexParser parser = new RegexParser();
            String postfix = parser.infixToPostfix(regex);
            Stack<RegexNode> nodeStack = new Stack<>();

            for (char c : postfix.toCharArray()) {
                if (Character.isLetterOrDigit(c)) nodeStack.push(new LiteralNode(String.valueOf(c)));
                else if (c == '*') nodeStack.push(new StarNode(nodeStack.pop()));
                else if (c == '|') { RegexNode r = nodeStack.pop(); RegexNode l = nodeStack.pop(); nodeStack.push(new UnionNode(l, r)); }
                else if (c == '.') { RegexNode r = nodeStack.pop(); RegexNode l = nodeStack.pop(); nodeStack.push(new ConcatNode(l, r)); }
            }

            Automaton result = nodeStack.pop().transform();

            String safeName = regex.replaceAll("[^a-zA-Z0-9]", "");

            addAutomaton(result, "Regex: " + regex);
        } catch (Exception e) {
            throw new InvalidRegexException(regex);
        }
    }

    // Помощни методи, използващи същата логика като union/concat/un, но с уникални броячи
    public Automaton createBasicAutomaton(String symbol) {
        String s1 = "s" + (stateCounter++);
        String s2 = "s" + (stateCounter++);
        return new AutomatonBuilder("tmp" + (stateCounter++))
                .setInitial(s1).addFinal(s2).addTransition(s1, s2, symbol).build();
    }

    public Automaton applyIteration(Automaton a) {
        String prefix = "it" + (stateCounter++) + "_";
        Automaton res = new Automaton("un_" + a.getId());
        mergeWithUniqueStates(res, a, prefix);
        res.setInitialState(prefix + a.getInitialState());
        for (String f : a.getFinalStates()) {
            res.addFinalState(prefix + f);
            res.addTransition(new Transition(prefix + f, prefix + a.getInitialState(), "epsilon"));
        }
        return res;
    }

    public Automaton applyConcat(Automaton a1, Automaton a2) {
        Automaton res = new Automaton("concat_" + (stateCounter++));
        String p1 = "c1_" + (stateCounter++) + "_", p2 = "c2_" + (stateCounter++) + "_";
        mergeWithUniqueStates(res, a1, p1); mergeWithUniqueStates(res, a2, p2);
        res.setInitialState(p1 + a1.getInitialState());
        for (String f : a2.getFinalStates()) res.addFinalState(p2 + f);
        for (String f : a1.getFinalStates()) res.addTransition(new Transition(p1 + f, p2 + a2.getInitialState(), "epsilon"));
        return res;
    }

    public Automaton applyUnion(Automaton a1, Automaton a2) {
        Automaton res = new Automaton("union_" + (stateCounter++));
        String p1 = "u1_" + (stateCounter++) + "_", p2 = "u2_" + (stateCounter++) + "_";
        mergeWithUniqueStates(res, a1, p1); mergeWithUniqueStates(res, a2, p2);
        String start = "start" + (stateCounter++), end = "final" + (stateCounter++);
        res.setInitialState(start); res.addFinalState(end);
        res.addTransition(new Transition(start, p1 + a1.getInitialState(), "epsilon"));
        res.addTransition(new Transition(start, p2 + a2.getInitialState(), "epsilon"));
        for (String f : a1.getFinalStates()) res.addTransition(new Transition(p1 + f, end, "epsilon"));
        for (String f : a2.getFinalStates()) res.addTransition(new Transition(p2 + f, end, "epsilon"));
        return res;
    }

    private void mergeWithUniqueStates(Automaton target, Automaton source, String prefix) {
        for (Transition t : source.getTransitions()) {
            target.addTransition(new Transition(prefix + t.getFromState(), prefix + t.getToState(), t.getSymbol()));
            target.addState(prefix + t.getFromState()); target.addState(prefix + t.getToState());
        }
    }


    public void determinize(String id) throws AutomatonNotFoundException {
        Automaton nfa = findById(id);

        String newId = "det_" + id;
        Automaton dfa = new Automaton(newId);

        // Списък от символи в азбуката (без епсилон)
        Set<String> alphabet = new HashSet<>();
        for (Transition t : nfa.getTransitions()) {
            if (!isEpsilon(t.getSymbol())) alphabet.add(t.getSymbol());
        }

        // Речник за следене на новите състояния (Map: Множество от NFA състояния -> Име на DFA състояние)
        Map<Set<String>, String> dfaStatesMap = new HashMap<>();
        Queue<Set<String>> unprocessedStates = new LinkedList<>();

        // 1. Начално състояние на DFA е епсилон-затварянето на началното на NFA
        Set<String> startSet = epsilonClosure(Collections.singleton(nfa.getInitialState()), nfa);
        String dfaStartName = "D0";
        dfaStatesMap.put(startSet, dfaStartName);
        unprocessedStates.add(startSet);
        dfa.setInitialState(dfaStartName);
        dfa.addState(dfaStartName);

        int stateIdx = 1;
        while (!unprocessedStates.isEmpty()) {
            Set<String> currentSet = unprocessedStates.poll();
            String currentDfaName = dfaStatesMap.get(currentSet);

            // Проверка дали е финално (ако съдържа поне едно финално от NFA)
            for (String s : currentSet) {
                if (nfa.getFinalStates().contains(s)) {
                    dfa.addFinalState(currentDfaName);
                    break;
                }
            }

            // За всеки символ от азбуката търсим къде отиваме
            for (String symbol : alphabet) {
                Set<String> moveSet = new HashSet<>();
                for (String nfaState : currentSet) {
                    for (Transition t : nfa.getTransitions()) {
                        if (t.getFromState().equals(nfaState) && t.getSymbol().equals(symbol)) {
                            moveSet.add(t.getToState());
                        }
                    }
                }

                if (moveSet.isEmpty()) continue;

                Set<String> targetSet = epsilonClosure(moveSet, nfa);

                if (!dfaStatesMap.containsKey(targetSet)) {
                    String newDfaName = "D" + (stateIdx++);
                    dfaStatesMap.put(targetSet, newDfaName);
                    unprocessedStates.add(targetSet);
                    dfa.addState(newDfaName);
                }

                dfa.addTransition(new Transition(currentDfaName, dfaStatesMap.get(targetSet), symbol));
            }
        }

        loadedAutomata.add(dfa);
        System.out.println("Automaton determinized. New ID: " + newId);
    }
}