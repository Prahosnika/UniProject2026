package bg.tu_varna.sit.f24621702.task.models;

/** Изброим тип за видовете автомати. */
public enum AutomatonType {
    DFA("Детерминиран краен автомат"),
    NFA("Недетерминиран краен автомат");

    private final String description;
    AutomatonType(String description) { this.description = description; }
    public String getDescription() { return description; }
}