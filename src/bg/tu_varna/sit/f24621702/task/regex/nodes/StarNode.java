package bg.tu_varna.sit.f24621702.task.regex.nodes;

import bg.tu_varna.sit.f24621702.task.models.Automaton;
import bg.tu_varna.sit.f24621702.task.services.FileService;

/**
 * Представлява възел в дървото на регулярния израз, съответстващ на операцията
 * "Звезда на Клини" (итерация). Позволява езикът на подвъзела да се повтаря
 * нула или повече пъти.
 */
public class StarNode implements RegexNode {
    /** Подвъзелът, върху който се прилага итерацията. */
    private RegexNode child;

    /**
     * Конструктор за създаване на възел за итерация.
     * @param child Възелът, който ще бъде повторен.
     */
    public StarNode(RegexNode child) { this.child = child; }

    /**
     * Трансформира подвъзела в автомат и прилага върху него операцията за итерация.
     * @return Нов автомат, реализиращ положителна обвивка (итерация) на езика.
     */
    @Override
    public Automaton transform() {
        return FileService.getInstance().applyIteration(child.transform());
    }
}