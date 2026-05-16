package bg.tu_varna.sit.f24621702.task.models;

public enum SpecialSymbol {
    EPSILON("epsilon"),
    EMPTY_SET("empty");

    private final String value;
    SpecialSymbol(String value) { this.value = value; }
    public String getValue() { return value; }
}