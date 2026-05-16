package bg.tu_varna.sit.f24621702.task.commands;

import bg.tu_varna.sit.f24621702.task.interfaces.Command;
import bg.tu_varna.sit.f24621702.task.services.FileService;

/**
 * Команда за отваряне на файл и зареждане на автоматите от него.
 * Файлът се търси в системната директория за съхранение.
 */
public class OpenCommand implements Command {
    private final FileService fileService;

    public OpenCommand(FileService fileService) { this.fileService = fileService; }

    /**
     * Изпълнява отваряне на файл.
     * @param args Аргументи: [filename]
     */
    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: open <path>");
            return;
        }
        fileService.open(args[0]);
    }
}