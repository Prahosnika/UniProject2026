package bg.tu_varna.sit.f24621702.task.services.impl;

import bg.tu_varna.sit.f24621702.task.models.*;
import bg.tu_varna.sit.f24621702.task.services.base.AbstractAutomatonService;
import java.util.*;

/**
 * Услуга, имплементираща алгоритми за анализ и симулация на автомати.
 * Включва проверка на думи, проверка за празен език и детерминиране (Subset Construction).
 */
public class AnalysisService extends AbstractAutomatonService {

    /**
     * Симулира работата на автомат върху дадена дума.
     * Използва ε-затваряне за поддръжка на недетерминирани автомати (NFA).
     * @param a Автоматът за симулация.
     * @param word Думата, която се тества.
     * @return true, ако думата принадлежи на езика на автомата.
     */
    public boolean recognize(Automaton a, String word) {
        Set<String> currentStates = epsilonClosure(Collections.singleton(a.getInitialState()), a);

        for (char symbol : word.toCharArray()) {
            String sym = String.valueOf(symbol);
            Set<String> nextStates = new HashSet<>();
            for (String state : currentStates) {
                for (Transition t : a.getTransitions()) {
                    if (t.getFromState().equals(state) && t.getSymbol().equals(sym)) {
                        nextStates.add(t.getToState());
                    }
                }
            }
            currentStates = epsilonClosure(nextStates, a);
            if (currentStates.isEmpty()) break;
        }

        for (String state : currentStates) {
            if (a.getFinalStates().contains(state)) return true;
        }
        return false;
    }

    /**
     * Проверява дали езикът на автомата е празен (т.е. няма път до финално състояние).
     * Използва алгоритъм за обхождане в ширина (BFS).
     * @param a Автомат за проверка.
     * @return true, ако никое финално състояние не е достижимо.
     */
    public boolean isLanguageEmpty(Automaton a) {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(a.getInitialState());
        visited.add(a.getInitialState());

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (a.getFinalStates().contains(current)) return false;
            for (Transition t : a.getTransitions()) {
                if (t.getFromState().equals(current) && !visited.contains(t.getToState())) {
                    visited.add(t.getToState());
                    queue.add(t.getToState());
                }
            }
        }
        return true;
    }

    /**
     * Трансформира недетерминиран краен автомат (НКА) в детерминиран (ДКА).
     * Реализира алгоритъма за изграждане на подмножества (Subset Construction).
     * @param nfa Изходният недетерминиран автомат.
     * @return Нов детерминиран автомат.
     */
    public Automaton determinize(Automaton nfa) {
        Automaton dfa = new Automaton("det_" + nfa.getId());
        Set<String> alphabet = new HashSet<>();
        for (Transition t : nfa.getTransitions()) if (!isEpsilon(t.getSymbol())) alphabet.add(t.getSymbol());

        Map<Set<String>, String> dfaStatesMap = new HashMap<>();
        Queue<Set<String>> unprocessed = new LinkedList<>();

        Set<String> startSet = epsilonClosure(Collections.singleton(nfa.getInitialState()), nfa);
        dfaStatesMap.put(startSet, "D0");
        unprocessed.add(startSet);
        dfa.setInitialState("D0");
        dfa.addState("D0");

        int idx = 1;
        while (!unprocessed.isEmpty()) {
            Set<String> currentSet = unprocessed.poll();
            String currentName = dfaStatesMap.get(currentSet);

            for (String s : currentSet) {
                if (nfa.getFinalStates().contains(s)) {
                    dfa.addFinalState(currentName);
                    break;
                }
            }

            for (String sym : alphabet) {
                Set<String> move = new HashSet<>();
                for (String s : currentSet) {
                    for (Transition t : nfa.getTransitions()) {
                        if (t.getFromState().equals(s) && t.getSymbol().equals(sym)) move.add(t.getToState());
                    }
                }
                if (move.isEmpty()) continue;
                Set<String> target = epsilonClosure(move, nfa);
                if (!dfaStatesMap.containsKey(target)) {
                    String newName = "D" + (idx++);
                    dfaStatesMap.put(target, newName);
                    unprocessed.add(target);
                    dfa.addState(newName);
                }
                dfa.addTransition(new Transition(currentName, dfaStatesMap.get(target), sym));
            }
        }
        return dfa;
    }
}