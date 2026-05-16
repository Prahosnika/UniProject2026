package bg.tu_varna.sit.f24621702.task.visitor;

import bg.tu_varna.sit.f24621702.task.models.Automaton;
import bg.tu_varna.sit.f24621702.task.models.Transition;
import bg.tu_varna.sit.f24621702.task.models.Symbol; // ВАЖНО: Импортирай Enum-а

import java.util.HashSet;
import java.util.Set;

public class DeterministicVisitor implements AutomatonVisitor {
    private boolean result = true;

    @Override
    public void visit(Automaton a) {
        // Проверка за епсилон преходи чрез Enum
        for (Transition t : a.getTransitions()) {
            if (t.getSymbol().equalsIgnoreCase(Symbol.EPSILON.getLiteral())) {
                result = false;
                return;
            }
        }

        // Проверка за детерминираност (по един излизащ преход за символ)
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

    public boolean isDeterministic() { return result; }
}