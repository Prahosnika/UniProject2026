package bg.tu_varna.sit.f24621702.task.visitor;

import bg.tu_varna.sit.f24621702.task.models.Automaton;
import bg.tu_varna.sit.f24621702.task.models.Transition;

import java.util.HashSet;
import java.util.Set;

public class DeterministicVisitor implements AutomatonVisitor {
    private boolean result = true;

    @Override
    public void visit(Automaton a) {
        if (a.hasEpsilonTransitions()) { result = false; return; }
        for (String state : a.getStates()) {
            Set<String> symbols = new HashSet<>();
            for (Transition t : a.getTransitions()) {
                if (t.getFromState().equals(state)) {
                    if (symbols.contains(t.getSymbol())) { result = false; return; }
                    symbols.add(t.getSymbol());
                }
            }
        }
    }
    public boolean isDeterministic() { return result; }
}