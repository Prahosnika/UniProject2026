package bg.tu_varna.sit.f24621702.task.observer;
import bg.tu_varna.sit.f24621702.task.ui.ConsoleUI;

public class AuditLogger implements AutomatonObserver {
    @Override
    public void onAutomatonCreated(String id, String method) {
        ConsoleUI.printInfo("[AUDIT]: Нов автомат '" + id + "' беше генериран чрез " + method);
    }
}