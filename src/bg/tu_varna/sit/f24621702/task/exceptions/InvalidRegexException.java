package bg.tu_varna.sit.f24621702.task.exceptions;

public class InvalidRegexException extends AutomataException {
    public InvalidRegexException(String regex) {
        super("Невалиден регулярен израз: '" + regex + "'. Проверете скобите и операторите.");
    }
}