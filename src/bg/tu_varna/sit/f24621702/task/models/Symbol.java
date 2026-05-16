package bg.tu_varna.sit.f24621702.task.models;

public enum Symbol {
    EPSILON("epsilon"),
    EMPTY_SET("@");

    private final String literal;
    Symbol(String literal) { this.literal = literal; }
    public String getLiteral() { return literal; }

    @Override
    public String toString() { return literal; }
}