package bg.tu_varna.sit.f24621702.task.commands;

import bg.tu_varna.sit.f24621702.task.interfaces.Command;

import static bg.tu_varna.sit.f24621702.task.ui.ConsoleUI.RESET;
import static bg.tu_varna.sit.f24621702.task.ui.ConsoleUI.YELLOW;

public class HelpCommand implements Command {
    @Override
    public void execute(String[] args) {
        System.out.println(YELLOW + "\n--- СИСТЕМНИ КОМАНДИ ---" + RESET);
        System.out.println("open <file>    | Отваря XML файл от папка /db");
        System.out.println("save           | Записва промените в текущия файл");
        System.out.println("saveas <path>  | Записва всичко в нов файл");
        System.out.println("close          | Затваря текущия файл");
        System.out.println("exit           | Изход от програмата");

        System.out.println(YELLOW + "\n--- АНАЛИЗ И ПРОВЕРКА ---" + RESET);
        System.out.println("list           | Показва всички заредени автомати");
        System.out.println("print <id>     | Показва детайли за конкретен автомат");
        System.out.println("recognize <id> | Проверява дали дума се приема");
        System.out.println("empty <id>     | Проверява дали езикът е празен");
        System.out.println("deterministic  | Проверява за детерминираност");

        System.out.println(YELLOW + "\n--- ТРАНСФОРМАЦИИ (ГЕНЕРИРАНЕ) ---" + RESET);
        System.out.println("union <id1> <id2> | Създава обединение");
        System.out.println("concat <id1> <id2>| Създава конкатенация");
        System.out.println("un <id>           | Прилага итерация (Клини)");
        System.out.println("reg <regex>       | Създава автомат от регулярен израз");
        System.out.println("determinize <id>  | Превръща NFA в DFA");
    }
}