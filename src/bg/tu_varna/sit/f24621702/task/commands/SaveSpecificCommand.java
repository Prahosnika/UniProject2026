package bg.tu_varna.sit.f24621702.task.commands;

import bg.tu_varna.sit.f24621702.task.exceptions.AutomatonNotFoundException;
import bg.tu_varna.sit.f24621702.task.interfaces.Command;
import bg.tu_varna.sit.f24621702.task.services.FileService;

public class SaveSpecificCommand implements Command {
    private final FileService fileService;

    public SaveSpecificCommand(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public void execute(String[] args) throws AutomatonNotFoundException {
        // Тази команда очаква два аргумента: ID-то на автомата и името на новия файл
        if (args.length < 2) {
            System.out.println("Usage: save <id> <filename>");
            return;
        }

        String id = args[0];
        String fileName = args[1];
        fileService.saveSpecificAutomaton(id, fileName);
    }
}