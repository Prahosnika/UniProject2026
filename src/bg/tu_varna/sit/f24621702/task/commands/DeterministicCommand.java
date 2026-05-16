package bg.tu_varna.sit.f24621702.task.commands;

import bg.tu_varna.sit.f24621702.task.exceptions.AutomatonNotFoundException;
import bg.tu_varna.sit.f24621702.task.interfaces.Command;
import bg.tu_varna.sit.f24621702.task.services.FileService;

public class DeterministicCommand implements Command {
    private final FileService fileService;

    public DeterministicCommand(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public void execute(String[] args) throws AutomatonNotFoundException {
        if (args.length < 1) {
            System.out.println("Usage: deterministic <id>");
            return;
        }
        fileService.checkDeterministic(args[0]);
    }
}