package bg.tu_varna.sit.f24621702.task.commands;

import bg.tu_varna.sit.f24621702.task.exceptions.InvalidRegexException;
import bg.tu_varna.sit.f24621702.task.interfaces.Command;
import bg.tu_varna.sit.f24621702.task.services.FileService;

/**
 * Команда за генериране на автомат от регулярен израз.
 * Реализира алгоритъма на Томпсън чрез Composite Pattern.
 */
public class RegCommand implements Command {
    private final FileService fileService;

    public RegCommand(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Трансформира регулярен израз в нов автомат.
     * @param args Аргументи: [regex]
     * @throws InvalidRegexException Ако изразът е синтактично грешен.
     */
    @Override
    public void execute(String[] args) throws InvalidRegexException {
        if (args.length < 1) {
            System.out.println("Usage: reg <regex>");
            return;
        }
        try {
            fileService.fromRegex(args[0]);
        } catch (Exception e) {
            throw new InvalidRegexException(args[0]);
        }
    }
}