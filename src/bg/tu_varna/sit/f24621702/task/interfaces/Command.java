package bg.tu_varna.sit.f24621702.task.interfaces;
import bg.tu_varna.sit.f24621702.task.exceptions.AutomataException;

/**
 * Интерфейс за реализация на шаблона Command.
 */
public interface Command {
    /**
     * Изпълнява логиката на командата.
     * @param args Аргументи, подадени от потребителя.
     * @throws AutomataException При грешка по време на изпълнение.
     */
    void execute(String[] args) throws AutomataException;
}