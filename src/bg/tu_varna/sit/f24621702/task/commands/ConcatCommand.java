package bg.tu_varna.sit.f24621702.task.commands;

import bg.tu_varna.sit.f24621702.task.exceptions.AutomatonNotFoundException;
import bg.tu_varna.sit.f24621702.task.interfaces.Command;
import bg.tu_varna.sit.f24621702.task.services.FileService;

public class ConcatCommand implements Command {
    private final FileService fileService;

    public ConcatCommand(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public void execute(String[] args) throws AutomatonNotFoundException {
        // Конкатенацията изисква два автомата (id1 и id2)
        if (args.length < 2) {
            System.out.println("Usage: concat <id1> <id2>");
            return;
        }

        String id1 = args[0];
        String id2 = args[1];
        fileService.concat(id1, id2);
    }
}