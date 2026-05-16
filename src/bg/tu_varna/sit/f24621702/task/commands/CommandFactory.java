package bg.tu_varna.sit.f24621702.task.commands;

import bg.tu_varna.sit.f24621702.task.interfaces.Command;
import bg.tu_varna.sit.f24621702.task.services.FileService;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private final Map<String, Command> commands = new HashMap<>();

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
        commands.put("close", (args) -> fileService.close()); // Ламбда за по-простите команди

        // В CommandFactory за командата "save":
        commands.put("save", (args) -> {
            if (args.length == 0) {
                fileService.save(); // Системен запис на целия файл
            } else if (args.length == 2) {
                new SaveSpecificCommand(fileService).execute(args); // Запис на конкретен автомат
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

    public Command getCommand(String commandName) {
        return commands.get(commandName.toLowerCase());
    }
}