package bg.tu_varna.sit.f24621702.task.exceptions;

public class AutomatonNotFoundException extends AutomataException {
    public AutomatonNotFoundException(String id) {
        super("Автомат с идентификатор '" + id + "' не беше намерен в текущата сесия.");
    }
}