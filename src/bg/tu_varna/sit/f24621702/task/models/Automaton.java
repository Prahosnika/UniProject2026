package bg.tu_varna.sit.f24621702.task.models;

import bg.tu_varna.sit.f24621702.task.visitor.AutomatonVisitor;
import java.util.ArrayList;
import java.util.List;

/**
 * Клас, представляващ модел на краен автомат (НКА или ДКА).
 */
public class Automaton {
    private String id;
    private List<String> states;
    private List<String> alphabet;
    private List<Transition> transitions;
    private String initialState;
    private List<String> finalStates;
    private AutomatonType type;

    /**
     * Определя типа на автомата динамично.
     * @return AutomatonType (NFA ако има ε-преходи, иначе DFA).
     */
    public AutomatonType getType() {
        return hasEpsilonTransitions() ? AutomatonType.NFA : AutomatonType.DFA;
    }

    /**
     * Създава нов автомат с ID.
     * @param id Уникален идентификатор.
     */
    public Automaton(String id) {
        this.id = id;
        this.states = new ArrayList<>();
        this.alphabet = new ArrayList<>();
        this.transitions = new ArrayList<>();
        this.finalStates = new ArrayList<>();
    }

    // Стандартни гетери и сетери
    public String getId() { return id; }
    public List<Transition> getTransitions() { return transitions; }
    public void addTransition(Transition t) { transitions.add(t); }
    public void setInitialState(String state) { this.initialState = state; }

    /**
     * Добавя ново състояние в списъка, ако не съществува.
     * @param state Име на състоянието.
     */
    public void addState(String state) {
        if (!states.contains(state)) {
            states.add(state);
        }
    }
    public void addFinalState(String state) { finalStates.add(state); }
    public void addAlphabetSymbol(String symbol) { alphabet.add(symbol); }

    /**
     * Извежда информация за ID и преходите на автомата в конзолата.
     */
    public void printInfo() {
        System.out.println("Automaton ID: " + id);
        System.out.println("Transitions:");
        for (Transition t : transitions) {
            System.out.println(t);
        }
    }

    public List<String> getFinalStates() { return finalStates; }
    public String getInitialState() { return initialState; }
    public List<String> getAlphabet() { return alphabet; }
    public List<String> getStates() { return states; }

    /**
     * Проверява дали в автомата съществуват ε-преходи.
     * @return true ако има преход със символ "epsilon", "ε" или празен низ.
     */
    public boolean hasEpsilonTransitions() {
        for (Transition t : transitions) {
            if (t.getSymbol().equalsIgnoreCase("epsilon") || t.getSymbol().equals("ε") || t.getSymbol().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Приема посетител за извършване на анализ (Visitor Pattern).
     * @param visitor Обектът посетител.
     */
    public void accept(AutomatonVisitor visitor) {
        visitor.visit(this);
    }
}