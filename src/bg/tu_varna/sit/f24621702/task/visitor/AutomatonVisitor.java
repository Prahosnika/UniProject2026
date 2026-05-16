package bg.tu_varna.sit.f24621702.task.visitor;
import bg.tu_varna.sit.f24621702.task.models.Automaton;

/**
 * Интерфейс за реализация на шаблона Visitor.
 * Позволява добавяне на нови операции за анализ на автомати, без да се променя техния клас.
 */
public interface AutomatonVisitor {
    /**
     * Посещава обекта и извършва специфичен анализ.
     * @param automaton Автоматът, който се анализира.
     */
    void visit(Automaton automaton);
}