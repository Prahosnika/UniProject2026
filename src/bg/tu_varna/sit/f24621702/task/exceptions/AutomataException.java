package bg.tu_varna.sit.f24621702.task.exceptions;

/**
 * Базово изключение за всички логически грешки в приложението за автомати.
 */
public class AutomataException extends Exception {
    /**
     * Създава ново изключение с описателно съобщение.
     * @param message Описание на грешката.
     */
    public AutomataException(String message) { super(message); }
}