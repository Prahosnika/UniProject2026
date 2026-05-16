package bg.tu_varna.sit.f24621702.task.observer;
import bg.tu_varna.sit.f24621702.task.ui.ConsoleUI;

/**
 * Логър, който наблюдава създаването на нови автомати и съобщава в конзолата.
 */
public class AuditLogger implements AutomatonObserver {
    /**
     * Извиква се при регистриране на нов автомат.
     * @param id Идентификатор.
     * @param method Методът на генериране.
     */
    @Override
    public void onAutomatonCreated(String id, String method) {
        ConsoleUI.printInfo("[AUDIT]: Нов автомат '" + id + "' беше генериран чрез " + method);
    }
}