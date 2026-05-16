package bg.tu_varna.sit.f24621702.task.states;
import bg.tu_varna.sit.f24621702.task.ui.ConsoleUI;

public class OpenFileState implements FileState {
    @Override public void list() { /* вече се вика директно в FileService */ }
    @Override public void save() { /* вече се вика директно в FileService */ }
    @Override public void open(String fileName) { ConsoleUI.printInfo("Вече има отворен файл. Затворете го първо."); }
}