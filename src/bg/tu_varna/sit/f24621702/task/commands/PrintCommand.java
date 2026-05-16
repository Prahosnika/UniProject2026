package bg.tu_varna.sit.f24621702.task.commands;

import bg.tu_varna.sit.f24621702.task.exceptions.AutomatonNotFoundException;
import bg.tu_varna.sit.f24621702.task.interfaces.Command;
import bg.tu_varna.sit.f24621702.task.services.FileService;

/**
 * Команда за извеждане на пълната информация за даден автомат (състояния, преходи, финални състояния).
 */
public class PrintCommand implements Command {
    private final FileService fileService;

    public PrintCommand(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Принтира детайли за автомат.
     * @param args Аргументи: [id]
     * @throws AutomatonNotFoundException Ако няма такъв автомат.
     */
    @Override
    public void execute(String[] args) throws AutomatonNotFoundException {
        if (args.length < 1) {
            System.out.println("Usage: print <id>");
            return;
        }
        fileService.printAutomaton(args[0]);
    }
}