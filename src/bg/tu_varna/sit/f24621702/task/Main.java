package bg.tu_varna.sit.f24621702.task;

import bg.tu_varna.sit.f24621702.task.core.Engine;
import bg.tu_varna.sit.f24621702.task.exceptions.AutomataException;

/**
 * Главният клас на приложението, служещ за входна точка (Entry Point).
 * Този клас инициира стартирането на системния двигател.
 *
 * @author f24621702
 * @version 1.0
 */
public class Main {
    /**
     * Основен метод, който стартира изпълнението на програмата.
     * Използва Singleton инстанцията на Engine за задвижване на потребителския интерфейс.
     *
     * @param args Аргументи от командния ред (не се използват в текущата версия).
     * @throws AutomataException При възникване на фатална грешка по време на работа.
     */
    public static void main(String[] args) throws AutomataException {
        Engine.getInstance().run();
    }
}