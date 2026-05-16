package bg.tu_varna.sit.f24621702.task.commands;

import bg.tu_varna.sit.f24621702.task.exceptions.AutomatonNotFoundException;
import bg.tu_varna.sit.f24621702.task.interfaces.Command;
import bg.tu_varna.sit.f24621702.task.services.FileService;

/**
 * Команда за запис на един конкретен автомат в нов XML файл.
 * Полезно за експортиране на резултатите от трансформациите.
 */
public class SaveSpecificCommand implements Command {
    private final FileService fileService;

    public SaveSpecificCommand(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Експортира автомат във файл.
     * @param args Аргументи: [id, filename]
     * @throws AutomatonNotFoundException Ако ID-то е грешно.
     */
    @Override
    public void execute(String[] args) throws AutomatonNotFoundException {
        if (args.length < 2) {
            System.out.println("Usage: save <id> <filename>");
            return;
        }
        String id = args[0];
        String fileName = args[1];
        fileService.saveSpecificAutomaton(id, fileName);
    }
}