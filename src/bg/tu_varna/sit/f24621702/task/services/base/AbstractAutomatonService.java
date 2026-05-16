package bg.tu_varna.sit.f24621702.task.services.base;

import bg.tu_varna.sit.f24621702.task.models.Automaton;
import bg.tu_varna.sit.f24621702.task.models.Symbol;
import bg.tu_varna.sit.f24621702.task.models.Transition;
import java.util.*;

/**
 * Абстрактен базов клас за всички услуги, работещи с крайни автомати.
 * Осигурява споделена функционалност за управление на състоянията и работа с ε-преходи.
 */
public abstract class AbstractAutomatonService {
    /** Статичен брояч за генериране на уникални имена на състояния в рамките на цялото приложение. */
    protected static int stateCounter = 0;

    /** Път до директорията, в която се съхраняват файловете с данни. */
    protected final String STORAGE_DIR = "db/";

    /**
     * Проверява дали даден символ представлява ε-преход (празен преход).
     * @param sym Символ за проверка.
     * @return true, ако символът съвпада с дефиницията за ε в Symbol Enum или е празен.
     */
    protected boolean isEpsilon(String sym) {
        return sym.equalsIgnoreCase(Symbol.EPSILON.getLiteral()) || sym.isEmpty();
    }

    /**
     * Копира състоянията и преходите от изходен автомат в целеви,
     * като добавя уникален префикс за избягване на конфликти в имената.
     * @param target Автоматът, в който се копира.
     * @param source Автоматът, от който се вземат данните.
     * @param prefix Низ, който се добавя пред името на всяко състояние.
     */
    protected void mergeWithUniqueStates(Automaton target, Automaton source, String prefix) {
        for (Transition t : source.getTransitions()) {
            target.addTransition(new Transition(prefix + t.getFromState(), prefix + t.getToState(), t.getSymbol()));
            target.addState(prefix + t.getFromState());
            target.addState(prefix + t.getToState());
        }
    }

    /**
     * Изчислява ε-затварянето (epsilon closure) за дадено множество от състояния.
     * Намира всички състояния, достижими от текущите само чрез ε-преходи.
     * @param states Първоначално множество от състояния.
     * @param a Автоматът, върху който се извършва операцията.
     * @return Множество от всички достижими състояния.
     */
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