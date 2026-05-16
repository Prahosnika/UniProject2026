package bg.tu_varna.sit.f24621702.task.ui;

/**
 * Помощен клас за управление на потребителския интерфейс в конзолата.
 * Осигурява средства за оцветяване на текста, извеждане на банери и стандартизирани съобщения.
 */
public class ConsoleUI {
    /**
     * Изброим тип за ANSI цветови кодове, използвани за форматиране на конзолния изход.
     */
    public enum Color {
        RESET("\u001B[0m"),
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        PURPLE("\u001B[35m"),
        CYAN("\u001B[36m");

        private final String code;
        Color(String code) { this.code = code; }
        @Override
        public String toString() { return code; }
    }

    /** Константи за бърз достъп до цветовете чрез статично импортиране. */
    public static final String RESET = Color.RESET.toString();
    public static final String RED = Color.RED.toString();
    public static final String GREEN = Color.GREEN.toString();
    public static final String YELLOW = Color.YELLOW.toString();
    public static final String BLUE = Color.BLUE.toString();
    public static final String PURPLE = Color.PURPLE.toString();
    public static final String CYAN = Color.CYAN.toString();

    /** Извежда приветствен банер на програмата. */
    public static void printBanner() {
        System.out.println(CYAN + "==================================================");
        System.out.println("   AUTOMATA PRO: СИСТЕМА ЗА УПРАВЛЕНИЕ НА НКА    ");
        System.out.println("        ТУ-Варна | Фак. № 24621702               ");
        System.out.println("==================================================" + RESET);
    }

    /** @param message Съобщение за успешно изпълнена операция. */
    public static void printSuccess(String message) {
        System.out.println(GREEN + "✔ [SUCCESS]: " + RESET + message);
    }

    /** @param message Съобщение за възникнала грешка. */
    public static void printError(String message) {
        System.out.println(RED + "✘ [ERROR]: " + RESET + message);
    }

    /** @param message Информационно съобщение. */
    public static void printInfo(String message) {
        System.out.println(BLUE + "ℹ [INFO]: " + RESET + message);
    }

    /** Принтира разделителна линия. */
    public static void printDivider() {
        System.out.println(CYAN + "--------------------------------------------------" + RESET);
    }
}