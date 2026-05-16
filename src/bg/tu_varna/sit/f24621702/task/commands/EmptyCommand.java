package bg.tu_varna.sit.f24621702.task.commands;

import bg.tu_varna.sit.f24621702.task.exceptions.AutomatonNotFoundException;
import bg.tu_varna.sit.f24621702.task.interfaces.Command;
import bg.tu_varna.sit.f24621702.task.services.FileService;

/**
 * Команда за проверка дали езикът, разпознаван от автомата, е празен.
 * Използва алгоритъм за търсене на път от началното до някое финално състояние.
 */
public class EmptyCommand implements Command {
    private final FileService fileService;
    public EmptyCommand(FileService fileService) { this.fileService = fileService; }

    /**
     * Изпълнява проверката за празен език.
     * @param args Аргументи: [id]
     * @throws AutomatonNotFoundException Ако ID-то е невалидно.
     */
    @Override
    public void execute(String[] args) throws AutomatonNotFoundException {
        if (args.length < 1) { System.out.println("Usage: empty <id>"); return; }
        fileService.checkEmpty(args[0]);
    }
}