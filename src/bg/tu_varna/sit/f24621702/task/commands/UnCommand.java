package bg.tu_varna.sit.f24621702.task.commands;

import bg.tu_varna.sit.f24621702.task.exceptions.AutomatonNotFoundException;
import bg.tu_varna.sit.f24621702.task.interfaces.Command;
import bg.tu_varna.sit.f24621702.task.services.FileService;

/**
 * Команда за прилагане на итерация (Клини - положителна обвивка) върху автомат.
 */
public class UnCommand implements Command {
    private final FileService fileService;

    public UnCommand(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Генерира итерация на автомата.
     * @param args Аргументи: [id]
     * @throws AutomatonNotFoundException Ако автоматът не съществува.
     */
    @Override
    public void execute(String[] args) throws AutomatonNotFoundException {
        if (args.length < 1) {
            System.out.println("Usage: un <id>");
            return;
        }
        fileService.iteration(args[0]);
    }
}