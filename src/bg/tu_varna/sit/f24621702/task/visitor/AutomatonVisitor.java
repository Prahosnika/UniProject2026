package bg.tu_varna.sit.f24621702.task.visitor;
import bg.tu_varna.sit.f24621702.task.models.Automaton;

public interface AutomatonVisitor {
    void visit(Automaton automaton);
}