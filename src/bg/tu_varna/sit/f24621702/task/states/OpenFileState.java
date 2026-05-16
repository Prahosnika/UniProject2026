package bg.tu_varna.sit.f24621702.task.states;

import bg.tu_varna.sit.f24621702.task.ui.ConsoleUI;

/**
 * Състояние, представляващо активна сесия с успешно зареден файл.
 * В това състояние потребителят има достъп до всички операции над автоматите.
 */
public class OpenFileState implements FileState {

    /** Логиката за листване е достъпна и се управлява от FileService. */
    @Override public void list() { /* вече се вика директно в FileService */ }

    /** Логиката за запис е достъпна и се управлява от FileService. */
    @Override public void save() { /* вече се вика директно в FileService */ }

    /**
     * Предотвратява отварянето на нов файл, докато текущият не бъде затворен.
     * @param fileName Име на файла.
     */
    @Override
    public void open(String fileName) {
        ConsoleUI.printInfo("Вече има отворен файл. Затворете го първо чрез командата 'close'.");
    }
}