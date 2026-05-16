package bg.tu_varna.sit.f24621702.task.commands;

import bg.tu_varna.sit.f24621702.task.interfaces.Command;
import bg.tu_varna.sit.f24621702.task.services.FileService;

public class ListCommand implements Command {
    private final FileService fileService;

    public ListCommand(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public void execute(String[] args) {
        fileService.listAutomata();
    }
}