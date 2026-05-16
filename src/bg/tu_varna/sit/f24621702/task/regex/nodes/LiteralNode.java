package bg.tu_varna.sit.f24621702.task.regex.nodes;
import bg.tu_varna.sit.f24621702.task.models.Automaton;
import bg.tu_varna.sit.f24621702.task.services.FileService;

public class LiteralNode implements RegexNode {
    private String symbol;
    public LiteralNode(String symbol) { this.symbol = symbol; }
    @Override
    public Automaton transform() {
        return FileService.getInstance().createBasicAutomaton(symbol);
    }
}