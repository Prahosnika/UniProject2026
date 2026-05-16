package bg.tu_varna.sit.f24621702.task.models;

public enum Symbol {
    EPSILON("epsilon"),
    EMPTY("@");

    private final String literal;
    Symbol(String literal) { this.literal = literal; }
    public String getLiteral() { return literal; }
}