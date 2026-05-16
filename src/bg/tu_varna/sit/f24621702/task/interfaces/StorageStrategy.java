package bg.tu_varna.sit.f24621702.task.interfaces;

import bg.tu_varna.sit.f24621702.task.models.Automaton;

import java.util.List;

public interface StorageStrategy {
    List<Automaton> load(String path) throws Exception;
    void save(String path, List<Automaton> automata) throws Exception;
}