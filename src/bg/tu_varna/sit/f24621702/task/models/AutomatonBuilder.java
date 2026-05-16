package bg.tu_varna.sit.f24621702.task.models;

/**
 * Строител (Builder) за по-лесно създаване на обекти от тип Automaton.
 */
public class AutomatonBuilder {
    private Automaton automaton;

    /** @param id Идентификатор на новия автомат. */
    public AutomatonBuilder(String id) {
        this.automaton = new Automaton(id);
    }

    /** Дефинира начално състояние. */
    public AutomatonBuilder setInitial(String state) {
        automaton.setInitialState(state);
        automaton.addState(state);
        return this;
    }

    /** Добавя финално състояние. */
    public AutomatonBuilder addFinal(String state) {
        automaton.addFinalState(state);
        automaton.addState(state);
        return this;
    }

    /** Добавя преход и регистрира съответните състояния. */
    public AutomatonBuilder addTransition(String from, String to, String symbol) {
        automaton.addTransition(new Transition(from, to, symbol));
        automaton.addState(from);
        automaton.addState(to);
        return this;
    }

    /** @return Изграденият обект Automaton. */
    public Automaton build() {
        return this.automaton;
    }
}