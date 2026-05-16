package bg.tu_varna.sit.f24621702.task.regex.nodes;
import bg.tu_varna.sit.f24621702.task.models.Automaton;

/**
 * Интерфейс за компонент в дървото на регулярния израз (Composite Pattern).
 * Всеки възел в дървото може да се трансформира в обект от тип Automaton.
 */
public interface RegexNode {
    /**
     * Трансформира възела в автомат.
     * @return Обект от тип Automaton, съответстващ на логиката на възела.
     */
    Automaton transform();
}