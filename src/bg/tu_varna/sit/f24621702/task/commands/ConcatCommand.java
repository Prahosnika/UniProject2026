package bg.tu_varna.sit.f24621702.task.commands;

import bg.tu_varna.sit.f24621702.task.exceptions.AutomatonNotFoundException;
import bg.tu_varna.sit.f24621702.task.interfaces.Command;
import bg.tu_varna.sit.f24621702.task.services.FileService;

/**
 * Команда за извършване на конкатенация на два автомата.
 * Резултатът е нов автомат, който приема думи, образувани от дума от първия и дума от втория автомат.
 */
public class ConcatCommand implements Command {
    private final FileService fileService;

    /**
     * Конструктор на командата.
     * @param fileService Услугата за автомати.
     */
    public ConcatCommand(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Изпълнява конкатенация между два автомата по подадени ID-та.
     *
     * @param args Аргументи: [id1, id2]
     * @throws AutomatonNotFoundException Ако някой от автоматите не е намерен.
     */
    @Override
    public void execute(String[] args) throws AutomatonNotFoundException {
        if (args.length < 2) {
            System.out.println("Usage: concat <id1> <id2>");
            return;
        }
        fileService.concat(args[0], args[1]);
    }
}