package bg.tu_varna.sit.f24621702.task.states;
import bg.tu_varna.sit.f24621702.task.services.FileService;

public class ClosedFileState implements FileState {
    @Override public void list() {}
    @Override public void save() {}
    @Override public void open(String fileName) { FileService.getInstance().performOpen(fileName); }
}