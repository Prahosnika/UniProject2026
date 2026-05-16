package bg.tu_varna.sit.f24621702.task.commands;

import bg.tu_varna.sit.f24621702.task.exceptions.AutomatonNotFoundException;
import bg.tu_varna.sit.f24621702.task.interfaces.Command;
import bg.tu_varna.sit.f24621702.task.services.FileService;

/**
 * Команда за проверка дали дадена дума се разпознава (приема) от конкретен автомат.
 */
public class RecognizeCommand implements Command {
    private final FileService fileService;

    public RecognizeCommand(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Изпълнява симулация на думата върху автомата.
     * @param args Аргументи: [id, word]
     * @throws AutomatonNotFoundException Ако автоматът липсва.
     */
    @Override
    public void execute(String[] args) throws AutomatonNotFoundException {
        if (args.length < 2) {
            System.out.println("Usage: recognize <id> <word>");
            return;
        }
        fileService.recognize(args[0], args[1]);
    }
}