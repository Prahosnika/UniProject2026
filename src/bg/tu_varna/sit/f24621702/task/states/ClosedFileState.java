package bg.tu_varna.sit.f24621702.task.states;

import bg.tu_varna.sit.f24621702.task.services.FileService;

/**
 * Състояние, представляващо затворена сесия (няма зареден файл в паметта).
 * В това състояние повечето операции са деактивирани или пренасочват към отваряне на файл.
 */
public class ClosedFileState implements FileState {

    /** Изпълнява се, когато няма отворен файл (обикновено не прави нищо). */
    @Override public void list() {}

    /** Изпълнява се, когато няма отворен файл (обикновено не прави нищо). */
    @Override public void save() {}

    /**
     * Позволява преминаване към състояние на отворен файл чрез зареждане на данни.
     * @param fileName Име на файла за зареждане.
     */
    @Override
    public void open(String fileName) {
        FileService.getInstance().performOpen(fileName);
    }
}