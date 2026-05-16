package bg.tu_varna.sit.f24621702.task.observer;

/** Интерфейс за наблюдатели (Observer Pattern). */
public interface AutomatonObserver {
    /** Метод, който се задейства при събитие за създаване. */
    void onAutomatonCreated(String id, String method);
}