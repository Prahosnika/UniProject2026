package bg.tu_varna.sit.f24621702.task.states;

public interface FileState {
    void list();
    void save();
    void open(String fileName);
}