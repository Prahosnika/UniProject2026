package bg.tu_varna.sit.f24621702.task.core;

import bg.tu_varna.sit.f24621702.task.commands.CommandFactory;
import bg.tu_varna.sit.f24621702.task.exceptions.AutomataException;
import bg.tu_varna.sit.f24621702.task.interfaces.Command;
import bg.tu_varna.sit.f24621702.task.services.FileService;
import bg.tu_varna.sit.f24621702.task.ui.ConsoleUI;

import java.util.Arrays;
import java.util.Scanner;

import static bg.tu_varna.sit.f24621702.task.ui.ConsoleUI.*;

/**
 * Основен клас за управление на жизнения цикъл на приложението.
 * Реализира шаблона Singleton и управлява четенето на входни данни и изпълнението на команди.
 */
public class Engine {
    private static Engine instance;
    private final FileService fileService;
    private final CommandFactory commandFactory;
    private final Scanner scanner;

    /**
     * Частен конструктор за инициализация на услугите и фабриката за команди.
     */
    private Engine() {
        this.fileService = FileService.getInstance();
        this.commandFactory = new CommandFactory(this.fileService);
        this.scanner = new Scanner(System.in);
    }

    /**
     * Връща единствената инстанция на Engine.
     * @return Инстанция на класа Engine.
     */
    public static Engine getInstance() {
        if (instance == null) instance = new Engine();
        return instance;
    }

    /**
     * Стартира основния цикъл на програмата. Чете вход от конзолата,
     * парсва аргументите и извиква съответните команди.
     * @throws AutomataException при грешка в работата на автоматите.
     */
    public void run() throws AutomataException {
        ConsoleUI.printBanner();

        while (true) {
            System.out.print(PURPLE + "admin@automata" + RESET + ":" + CYAN + "~" + RESET + "$ ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            String[] parts = input.split("\\s+");
            String commandName = parts[0].toLowerCase();
            String[] args = Arrays.copyOfRange(parts, 1, parts.length);

            Command command = commandFactory.getCommand(commandName);
            if (command != null) {
                // Списък с команди, които могат да се изпълняват без отворен файл
                boolean isSystemCommand = commandName.equals("open") ||
                        commandName.equals("help") ||
                        commandName.equals("exit");

                if (!isSystemCommand && !fileService.isOpen()) {
                    ConsoleUI.printError("Моля, първо отворете файл с командата 'open <filename>'.");
                } else {
                    try {
                        command.execute(args);
                    } catch (AutomataException e) {
                        ConsoleUI.printError(e.getMessage());
                    } catch (Exception e) {
                        ConsoleUI.printError("Системна грешка: " + e.getMessage());
                    }
                }
            } else {
                ConsoleUI.printError("Неизвестна команда. Напишете 'help' за помощ.");
            }
        }
    }
}