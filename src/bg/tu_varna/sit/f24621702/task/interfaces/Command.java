package bg.tu_varna.sit.f24621702.task.interfaces;
import bg.tu_varna.sit.f24621702.task.exceptions.AutomataException;

public interface Command {
    void execute(String[] args) throws AutomataException;
}