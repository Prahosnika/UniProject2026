package bg.tu_varna.sit.f24621702.task.commands;

import bg.tu_varna.sit.f24621702.task.exceptions.AutomatonNotFoundException;
import bg.tu_varna.sit.f24621702.task.interfaces.Command;
import bg.tu_varna.sit.f24621702.task.services.FileService;

public class EmptyCommand implements Command {
    private final FileService fileService;
    public EmptyCommand(FileService fileService) { this.fileService = fileService; }

    @Override
    public void execute(String[] args) throws AutomatonNotFoundException {
        if (args.length < 1) { System.out.println("Usage: empty <id>"); return; }
        fileService.checkEmpty(args[0]);
    }
}