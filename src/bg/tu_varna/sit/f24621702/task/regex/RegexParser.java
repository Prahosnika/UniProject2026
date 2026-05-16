package bg.tu_varna.sit.f24621702.task.regex;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Клас за парсване на регулярни изрази.
 * Превръща израза от Infix (стандартен) в Postfix (обратен полски запис) формат.
 */
public class RegexParser {
    /** Карта на приоритетите на операторите. */
    private static final Map<Character, Integer> precedence = new HashMap<>();
    static {
        precedence.put('*', 3);
        precedence.put('.', 2);
        precedence.put('|', 1);
    }

    /**
     * Добавя оператора за конкатенация '.' на местата, където тя е подразбираща се.
     * @param regex Първоначален израз.
     * @return Форматиран израз с явни оператори за конкатенация.
     */
    private String formatRegex(String regex) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < regex.length(); i++) {
            char c1 = regex.charAt(i);
            res.append(c1);
            if (i + 1 < regex.length()) {
                char c2 = regex.charAt(i + 1);
                if (c1 != '(' && c1 != '|' && (Character.isLetterOrDigit(c2) || c2 == '(')) {
                    res.append('.');
                }
            }
        }
        return res.toString();
    }

    /**
     * Трансформира израза в Postfix нотация за Thompson's construction.
     * @param regex Входящ израз.
     * @return Postfix низ.
     */
    public String infixToPostfix(String regex) {
        String formatted = formatRegex(regex);
        StringBuilder output = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (char c : formatted.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                output.append(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') output.append(stack.pop());
                stack.pop();
            } else {
                while (!stack.isEmpty() && precedence.getOrDefault(stack.peek(), 0) >= precedence.getOrDefault(c, 0)) {
                    output.append(stack.pop());
                }
                stack.push(c);
            }
        }
        while (!stack.isEmpty()) output.append(stack.pop());
        return output.toString();
    }
}