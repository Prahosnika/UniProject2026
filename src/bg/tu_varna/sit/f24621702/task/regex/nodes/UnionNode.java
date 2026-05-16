package bg.tu_varna.sit.f24621702.task.regex.nodes;

import bg.tu_varna.sit.f24621702.task.models.Automaton;
import bg.tu_varna.sit.f24621702.task.services.FileService;

public class UnionNode implements RegexNode {
    private RegexNode left;
    private RegexNode right;

    public UnionNode(RegexNode left, RegexNode right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Automaton transform() {
        // Рекурсивно трансформираме лявото и дясното поддърво и ги обединяваме
        return FileService.getInstance().applyUnion(left.transform(), right.transform());
    }
}