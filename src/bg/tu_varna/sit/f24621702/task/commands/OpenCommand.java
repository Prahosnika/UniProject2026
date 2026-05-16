package bg.tu_varna.sit.f24621702.task.commands;

import bg.tu_varna.sit.f24621702.task.interfaces.Command;
import bg.tu_varna.sit.f24621702.task.services.FileService;

public class OpenCommand implements Command {
    private final FileService fileService;

    public OpenCommand(FileService fileService) { this.fileService = fileService; }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: open <path>");
            return;
        }
        fileService.open(args[0]);
    }
}