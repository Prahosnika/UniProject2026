package bg.tu_varna.sit.f24621702.task.commands;

import bg.tu_varna.sit.f24621702.task.interfaces.Command;
import bg.tu_varna.sit.f24621702.task.services.FileService;
import java.util.HashMap;
import java.util.Map;

/**
 * Фабрика за създаване и управление на потребителски команди.
 * Класът инициализира всички налични команди и ги съхранява в карта (Map) за бърз достъп.
 * Използва шаблона "Factory" за разделяне на логиката по създаване от логиката по изпълнение.
 */
public class CommandFactory {
    /** Списък с регистрирани команди, където ключът е името на командата в малки букви. */
    private final Map<String, Command> commands = new HashMap<>();

    /**
     * Конструктор на фабриката. Инициализира всички команди и ги асоциира с техните ключове.
     *
     * @param fileService Услугата, която предоставя бизнес логиката за работа с автомати.
     */
    public CommandFactory(FileService fileService) {
        commands.put("list", new ListCommand(fileService));
        commands.put("print", new PrintCommand(fileService));
        commands.put("save_single", new SaveSpecificCommand(fileService));
        commands.put("empty", new EmptyCommand(fileService));
        commands.put("deterministic", new DeterministicCommand(fileService));
        commands.put("recognize", new RecognizeCommand(fileService));
        commands.put("union", new UnionCommand(fileService));
        commands.put("concat", new ConcatCommand(fileService));
        commands.put("un", new UnCommand(fileService));
        commands.put("reg", new RegCommand(fileService));
        commands.put("determinize", new DeterminizeCommand(fileService));
        commands.put("open", new OpenCommand(fileService));
        commands.put("help", new HelpCommand());
        commands.put("close", (args) -> fileService.close());

        commands.put("save", (args) -> {
            if (args.length == 0) {
                fileService.save();
            } else if (args.length == 2) {
                try {
                    new SaveSpecificCommand(fileService).execute(args);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("Usage: save OR save <id> <filename>");
            }
        });

        commands.put("saveas", (args) -> {
            if (args.length > 0) fileService.saveAs(args[0]);
            else System.out.println("Usage: saveas <path>");
        });

        commands.put("exit", (args) -> {
            System.out.println("Exiting the program...");
            System.exit(0);
        });
    }

    /**
     * Намира команда по нейното име.
     *
     * @param commandName Името на командата (независимо от регистъра).
     * @return Обект, имплементиращ интерфейса Command, или null ако не е намерена.
     */
    public Command getCommand(String commandName) {
        return commands.get(commandName.toLowerCase());
    }
}