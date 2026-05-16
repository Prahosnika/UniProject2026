package bg.tu_varna.sit.f24621702.task.commands;

import bg.tu_varna.sit.f24621702.task.exceptions.InvalidRegexException;
import bg.tu_varna.sit.f24621702.task.interfaces.Command;
import bg.tu_varna.sit.f24621702.task.services.FileService;

public class RegCommand implements Command {
    private final FileService fileService;

    public RegCommand(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public void execute(String[] args) throws InvalidRegexException {
        if (args.length < 1) {
            System.out.println("Usage: reg <regex>");
            return;
        }
        fileService.fromRegex(args[0]);
    }
}