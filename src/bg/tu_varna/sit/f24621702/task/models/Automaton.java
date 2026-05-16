package bg.tu_varna.sit.f24621702.task.models;

import bg.tu_varna.sit.f24621702.task.visitor.AutomatonVisitor;

import java.util.ArrayList;
import java.util.List;

public class Automaton {
    private String id;
    private List<String> states;
    private List<String> alphabet;
    private List<Transition> transitions;
    private String initialState;
    private List<String> finalStates;

    private AutomatonType type;

    public AutomatonType getType() {
        // Можем автоматично да изчисляваме типа спрямо преходите
        return hasEpsilonTransitions() ? AutomatonType.NFA : AutomatonType.DFA;
    }

    public Automaton(String id) {
        this.id = id;
        this.states = new ArrayList<>();
        this.alphabet = new ArrayList<>();
        this.transitions = new ArrayList<>();
        this.finalStates = new ArrayList<>();
    }

    // Getters и Setters
    public String getId() { return id; }
    public List<Transition> getTransitions() { return transitions; }
    public void addTransition(Transition t) { transitions.add(t); }
    public void setInitialState(String state) { this.initialState = state; }
    public void addState(String state) {
        if (!states.contains(state)) {
            states.add(state);
        }
    }
    public void addFinalState(String state) { finalStates.add(state); }
    public void addAlphabetSymbol(String symbol) { alphabet.add(symbol); }

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

    // Проверка за епсилон преходи
    public boolean hasEpsilonTransitions() {
        for (Transition t : transitions) {
            if (t.getSymbol().equalsIgnoreCase("epsilon") || t.getSymbol().equals("ε") || t.getSymbol().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public void accept(AutomatonVisitor visitor) {
        visitor.visit(this);
    }
}