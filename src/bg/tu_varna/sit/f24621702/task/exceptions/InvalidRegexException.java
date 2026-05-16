package bg.tu_varna.sit.f24621702.task.exceptions;

/**
 * Изключение за синтактични грешки при обработка на регулярен израз.
 */
public class InvalidRegexException extends AutomataException {
    /**
     * @param regex Невалидният регулярен израз.
     */
    public InvalidRegexException(String regex) {
        super("Невалиден регулярен израз: '" + regex + "'. Проверете скобите и операторите.");
    }
}