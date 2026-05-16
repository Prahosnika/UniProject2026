package bg.tu_varna.sit.f24621702.task.commands;

import bg.tu_varna.sit.f24621702.task.interfaces.Command;
import bg.tu_varna.sit.f24621702.task.services.FileService;

/**
 * Команда за изброяване на идентификаторите на всички заредени автомати в текущата сесия.
 */
public class ListCommand implements Command {
    private final FileService fileService;

    public ListCommand(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Извиква метода за листване от FileService.
     * @param args Не се очакват аргументи.
     */
    @Override
    public void execute(String[] args) {
        fileService.listAutomata();
    }
}