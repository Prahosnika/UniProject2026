package bg.tu_varna.sit.f24621702.task.exceptions;

/**
 * Изключение, което се хвърля, когато търсен автомат не съществува в паметта.
 */
public class AutomatonNotFoundException extends AutomataException {
    /**
     * Конструира изключението с конкретно ID.
     * @param id Идентификаторът на автомата, който липсва.
     */
    public AutomatonNotFoundException(String id) {
        super("Автомат с идентификатор '" + id + "' не беше намерен в текущата сесия.");
    }
}