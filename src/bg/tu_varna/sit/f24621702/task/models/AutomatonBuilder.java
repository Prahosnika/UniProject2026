package bg.tu_varna.sit.f24621702.task.models;

public class AutomatonBuilder {
    private Automaton automaton;

    public AutomatonBuilder(String id) {
        this.automaton = new Automaton(id);
    }

    public AutomatonBuilder setInitial(String state) {
        automaton.setInitialState(state);
        automaton.addState(state);
        return this;
    }

    public AutomatonBuilder addFinal(String state) {
        automaton.addFinalState(state);
        automaton.addState(state);
        return this;
    }

    public AutomatonBuilder addTransition(String from, String to, String symbol) {
        automaton.addTransition(new Transition(from, to, symbol));
        automaton.addState(from);
        automaton.addState(to);
        return this;
    }

    public Automaton build() {
        return this.automaton;
    }
}