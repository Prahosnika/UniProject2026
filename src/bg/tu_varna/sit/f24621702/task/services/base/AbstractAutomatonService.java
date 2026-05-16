package bg.tu_varna.sit.f24621702.task.services.base;

import bg.tu_varna.sit.f24621702.task.models.Automaton;
import bg.tu_varna.sit.f24621702.task.models.Symbol;
import bg.tu_varna.sit.f24621702.task.models.Transition;
import java.util.*;

public abstract class AbstractAutomatonService {
    protected static int stateCounter = 0; // Статичен, за да се споделя от всички наследници
    protected final String STORAGE_DIR = "db/";

    protected boolean isEpsilon(String sym) {
        return sym.equalsIgnoreCase(Symbol.EPSILON.getLiteral()) || sym.isEmpty();
    }

    protected void mergeWithUniqueStates(Automaton target, Automaton source, String prefix) {
        for (Transition t : source.getTransitions()) {
            target.addTransition(new Transition(prefix + t.getFromState(), prefix + t.getToState(), t.getSymbol()));
            target.addState(prefix + t.getFromState());
            target.addState(prefix + t.getToState());
        }
    }

    protected Set<String> epsilonClosure(Set<String> states, Automaton a) {
        Set<String> closure = new HashSet<>(states);
        Stack<String> stack = new Stack<>();
        stack.addAll(states);

        while (!stack.isEmpty()) {
            String current = stack.pop();
            for (Transition t : a.getTransitions()) {
                if (t.getFromState().equals(current) && isEpsilon(t.getSymbol())) {
                    if (!closure.contains(t.getToState())) {
                        closure.add(t.getToState());
                        stack.push(t.getToState());
                    }
                }
            }
        }
        return closure;
    }
}