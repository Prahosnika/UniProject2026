package bg.tu_varna.sit.f24621702.task.regex.nodes;
import bg.tu_varna.sit.f24621702.task.models.Automaton;
import bg.tu_varna.sit.f24621702.task.services.FileService;

/**
 * Представлява лист в дървото на регулярния израз (конкретен символ).
 */
public class LiteralNode implements RegexNode {
    private String symbol;

    /** @param symbol Символът от азбуката. */
    public LiteralNode(String symbol) { this.symbol = symbol; }

    /**
     * Извиква FileService за създаване на базов автомат за символа.
     * @return Автомат с два състояния и преход между тях.
     */
    @Override
    public Automaton transform() {
        return FileService.getInstance().createBasicAutomaton(symbol);
    }
}