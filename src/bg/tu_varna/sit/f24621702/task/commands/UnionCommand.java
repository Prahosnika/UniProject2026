package bg.tu_varna.sit.f24621702.task.commands;

import bg.tu_varna.sit.f24621702.task.exceptions.AutomatonNotFoundException;
import bg.tu_varna.sit.f24621702.task.interfaces.Command;
import bg.tu_varna.sit.f24621702.task.services.FileService;

public class UnionCommand implements Command {
    private final FileService fileService;

    public UnionCommand(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public void execute(String[] args) throws AutomatonNotFoundException {
        if (args.length < 2) {
            System.out.println("Usage: union <id1> <id2>");
            return;
        }
        fileService.union(args[0], args[1]);
    }
}