package bg.tu_varna.sit.f24621702.task.services.impl;

import bg.tu_varna.sit.f24621702.task.models.*;
import bg.tu_varna.sit.f24621702.task.services.base.AbstractAutomatonService;

/**
 * Услуга за извършване на структурни трансформации над автомати.
 * Реализира основните стъпки от конструкцията на Томпсън за изграждане на НКА.
 */
public class TransformationService extends AbstractAutomatonService {

    /**
     * Създава елементарен автомат за един символ.
     * @param symbol Символът от азбуката.
     * @return Автомат с начално състояние, крайно състояние и преход между тях.
     */
    public Automaton createBasicAutomaton(String symbol) {
        String s1 = "s" + (stateCounter++);
        String s2 = "s" + (stateCounter++);
        return new AutomatonBuilder("tmp" + (stateCounter++))
                .setInitial(s1).addFinal(s2).addTransition(s1, s2, symbol).build();
    }

    /**
     * Прилага итерация (Звезда на Клини) върху автомат.
     * Добавя ε-преходи от финалните състояния обратно към началното.
     * @param a Изходен автомат.
     * @return Модифициран автомат, приемащ повторения на езика.
     */
    public Automaton applyIteration(Automaton a) {
        String prefix = "it" + (stateCounter++) + "_";
        Automaton res = new Automaton("un_" + a.getId());
        mergeWithUniqueStates(res, a, prefix);
        res.setInitialState(prefix + a.getInitialState());
        res.addState(prefix + a.getInitialState());
        for (String f : a.getFinalStates()) {
            res.addFinalState(prefix + f);
            res.addTransition(new Transition(prefix + f, prefix + a.getInitialState(), Symbol.EPSILON.getLiteral()));
        }
        return res;
    }

    /**
     * Извършва конкатенация (последователно свързване) на два автомата.
     * Свързва финалните състояния на първия с началото на втория чрез ε-преходи.
     * @param a1 Първи автомат.
     * @param a2 Втори автомат.
     * @return Нов обединен автомат.
     */
    public Automaton applyConcat(Automaton a1, Automaton a2) {
        Automaton res = new Automaton("concat_" + (stateCounter++));
        String p1 = "c1_" + (stateCounter++) + "_", p2 = "c2_" + (stateCounter++) + "_";
        mergeWithUniqueStates(res, a1, p1);
        mergeWithUniqueStates(res, a2, p2);
        res.setInitialState(p1 + a1.getInitialState());
        for (String f : a2.getFinalStates()) res.addFinalState(p2 + f);
        for (String f : a1.getFinalStates()) res.addTransition(new Transition(p1 + f, p2 + a2.getInitialState(), Symbol.EPSILON.getLiteral()));
        return res;
    }

    /**
     * Извършва обединение (Union) на два автомата (оператор '|').
     * Създава ново начално състояние, което се разклонява към двете стари начала.
     * @param a1 Първа алтернатива.
     * @param a2 Втора алтернатива.
     * @return Нов автомат, разпознаващ езика на a1 или a2.
     */
    public Automaton applyUnion(Automaton a1, Automaton a2) {
        Automaton res = new Automaton("union_" + (stateCounter++));
        String p1 = "u1_" + (stateCounter++) + "_", p2 = "u2_" + (stateCounter++) + "_";
        mergeWithUniqueStates(res, a1, p1);
        mergeWithUniqueStates(res, a2, p2);
        String start = "start" + (stateCounter++), end = "final" + (stateCounter++);
        res.setInitialState(start); res.addFinalState(end);
        res.addState(start); res.addState(end);
        res.addTransition(new Transition(start, p1 + a1.getInitialState(), Symbol.EPSILON.getLiteral()));
        res.addTransition(new Transition(start, p2 + a2.getInitialState(), Symbol.EPSILON.getLiteral()));
        for (String f : a1.getFinalStates()) res.addTransition(new Transition(p1 + f, end, Symbol.EPSILON.getLiteral()));
        for (String f : a2.getFinalStates()) res.addTransition(new Transition(p2 + f, end, Symbol.EPSILON.getLiteral()));
        return res;
    }
}