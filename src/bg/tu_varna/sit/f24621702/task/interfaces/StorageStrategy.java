package bg.tu_varna.sit.f24621702.task.interfaces;

import bg.tu_varna.sit.f24621702.task.models.Automaton;
import java.util.List;

/**
 * Интерфейс за дефиниране на стратегия за съхранение на данни (Strategy Pattern).
 */
public interface StorageStrategy {
    /**
     * Зарежда автомати от източник.
     * @param path Път до източника.
     * @return Списък със заредени автомати.
     * @throws Exception при IO или парсинг грешка.
     */
    List<Automaton> load(String path) throws Exception;

    /**
     * Записва списък с автомати в източник.
     * @param path Път до дестинацията.
     * @param automata Списък за запис.
     * @throws Exception при грешка в записа.
     */
    void save(String path, List<Automaton> automata) throws Exception;
}