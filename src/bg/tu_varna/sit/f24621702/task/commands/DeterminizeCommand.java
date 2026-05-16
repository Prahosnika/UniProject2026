package bg.tu_varna.sit.f24621702.task.commands;

import bg.tu_varna.sit.f24621702.task.exceptions.AutomatonNotFoundException;
import bg.tu_varna.sit.f24621702.task.interfaces.Command;
import bg.tu_varna.sit.f24621702.task.services.FileService;

/**
 * Команда за трансформиране на недетерминиран краен автомат (НКА) в детерминиран (ДКА).
 */
public class DeterminizeCommand implements Command {
    private final FileService fileService;
    public DeterminizeCommand(FileService fileService) { this.fileService = fileService; }

    /**
     * Изпълнява алгоритъма за детерминиране.
     * @param args Аргументи: [id]
     * @throws AutomatonNotFoundException Ако автоматът не е зареден.
     */
    @Override
    public void execute(String[] args) throws AutomatonNotFoundException {
        if (args.length < 1) {
            System.out.println("Usage: determinize <id>");
            return;
        }
        fileService.determinize(args[0]);
    }
}