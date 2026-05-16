package bg.tu_varna.sit.f24621702.task.regex.nodes;

import bg.tu_varna.sit.f24621702.task.models.Automaton;
import bg.tu_varna.sit.f24621702.task.services.FileService;

/**
 * Представлява възел в дървото на регулярния израз за операцията обединение (избор).
 * Позволява разпознаване на думи, които принадлежат на езика на левия ИЛИ на десния подвъзел.
 */
public class UnionNode implements RegexNode {
    /** Първият възможен избор (алтернатива). */
    private RegexNode left;
    /** Вторият възможен избор (алтернатива). */
    private RegexNode right;

    /**
     * Конструктор за създаване на възел за обединение.
     * @param left Първият подвъзел.
     * @param right Вторият подвъзел.
     */
    public UnionNode(RegexNode left, RegexNode right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Рекурсивно трансформира лявото и дясното поддърво и ги обединяваме чрез епсилон преходи.
     * @return Автомат, разпознаващ обединението на двата езика.
     */
    @Override
    public Automaton transform() {
        // Рекурсивно трансформираме лявото и дясното поддърво и ги обединяваме
        return FileService.getInstance().applyUnion(left.transform(), right.transform());
    }
}