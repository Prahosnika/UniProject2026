package bg.tu_varna.sit.f24621702.task.regex.nodes;

import bg.tu_varna.sit.f24621702.task.models.Automaton;
import bg.tu_varna.sit.f24621702.task.services.FileService;

public class ConcatNode implements RegexNode {
    private RegexNode left;
    private RegexNode right;

    public ConcatNode(RegexNode left, RegexNode right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Automaton transform() {
        // Рекурсивно трансформираме лявото и дясното поддърво и ги свързваме последователно
        return FileService.getInstance().applyConcat(left.transform(), right.transform());
    }
}