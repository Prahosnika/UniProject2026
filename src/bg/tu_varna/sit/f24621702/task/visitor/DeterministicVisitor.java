package bg.tu_varna.sit.f24621702.task.visitor;

import bg.tu_varna.sit.f24621702.task.models.Automaton;
import bg.tu_varna.sit.f24621702.task.models.Transition;
import bg.tu_varna.sit.f24621702.task.models.Symbol;

import java.util.HashSet;
import java.util.Set;

/**
 * Конкретен посетител, който проверява дали даден автомат е детерминиран.
 * Критерии: няма ε-преходи и от всяко състояние излиза само по един преход за символ.
 */
public class DeterministicVisitor implements AutomatonVisitor {
    private boolean result = true;

    /**
     * Извършва анализ за детерминираност.
     * @param a Автоматът за проверка.
     */
    @Override
    public void visit(Automaton a) {
        // Проверка за епсилон преходи
        for (Transition t : a.getTransitions()) {
            if (t.getSymbol().equalsIgnoreCase(Symbol.EPSILON.getLiteral())) {
                result = false;
                return;
            }
        }

        // Проверка за уникалност на излизащите преходи
        for (String state : a.getStates()) {
            Set<String> symbols = new HashSet<>();
            for (Transition t : a.getTransitions()) {
                if (t.getFromState().equals(state)) {
                    if (symbols.contains(t.getSymbol())) {
                        result = false;
                        return;
                    }
                    symbols.add(t.getSymbol());
                }
            }
        }
    }

    /** @return true ако автоматът е детерминиран. */
    public boolean isDeterministic() { return result; }
}