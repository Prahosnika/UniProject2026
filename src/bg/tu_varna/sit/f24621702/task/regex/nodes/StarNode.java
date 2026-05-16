package bg.tu_varna.sit.f24621702.task.regex.nodes;
import bg.tu_varna.sit.f24621702.task.models.Automaton;
import bg.tu_varna.sit.f24621702.task.services.FileService;

public class StarNode implements RegexNode {
    private RegexNode child;
    public StarNode(RegexNode child) { this.child = child; }
    @Override
    public Automaton transform() {
        return FileService.getInstance().applyIteration(child.transform());
    }
}