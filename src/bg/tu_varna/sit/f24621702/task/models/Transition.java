package bg.tu_varna.sit.f24621702.task.models;

public class Transition {
    private String fromState;
    private String toState;
    private String symbol;

    public Transition(String fromState, String toState, String symbol) {
        this.fromState = fromState;
        this.toState = toState;
        this.symbol = symbol;
    }

    // Getters
    public String getFromState() { return fromState; }
    public String getToState() { return toState; }
    public String getSymbol() { return symbol; }

    @Override
    public String toString() {
        return String.format("  %s --(%s)--> %s", fromState, symbol, toState);
    }
}