package bg.tu_varna.sit.f24621702.task.regex.nodes;

import bg.tu_varna.sit.f24621702.task.models.Automaton;
import bg.tu_varna.sit.f24621702.task.services.FileService;

/**
 * Представлява възел в дървото на регулярния израз за операцията конкатенация.
 * Свързва два подвъзела последователно един след друг.
 */
public class ConcatNode implements RegexNode {
    /** Левият (първият) подвъзел в последователността. */
    private RegexNode left;
    /** Десният (вторият) подвъзел в последователността. */
    private RegexNode right;

    /**
     * Конструктор за създаване на възел за конкатенация.
     * @param left Първият подвъзел.
     * @param right Вторият подвъзел.
     */
    public ConcatNode(RegexNode left, RegexNode right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Рекурсивно трансформира лявото и дясното поддърво и ги свързва последователно.
     * @return Автомат, разпознаващ последователността от двата езика.
     */
    @Override
    public Automaton transform() {
        // Рекурсивно трансформираме лявото и дясното поддърво и ги свързваме последователно
        return FileService.getInstance().applyConcat(left.transform(), right.transform());
    }
}