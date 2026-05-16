package bg.tu_varna.sit.f24621702.task.commands;

import bg.tu_varna.sit.f24621702.task.exceptions.AutomatonNotFoundException;
import bg.tu_varna.sit.f24621702.task.interfaces.Command;
import bg.tu_varna.sit.f24621702.task.services.FileService;

/**
 * Команда за проверка дали даден автомат е детерминиран.
 * Използва Visitor шаблон за анализ на преходите на автомата.
 */
public class DeterministicCommand implements Command {
    private final FileService fileService;

    public DeterministicCommand(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Извиква проверка за детерминираност.
     * @param args Аргументи: [id]
     * @throws AutomatonNotFoundException Ако автоматът липсва.
     */
    @Override
    public void execute(String[] args) throws AutomatonNotFoundException {
        if (args.length < 1) {
            System.out.println("Usage: deterministic <id>");
            return;
        }
        fileService.checkDeterministic(args[0]);
    }
}