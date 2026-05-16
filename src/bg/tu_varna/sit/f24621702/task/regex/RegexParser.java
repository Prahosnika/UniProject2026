package bg.tu_varna.sit.f24621702.task.regex;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class RegexParser {
    // Дефинираме приоритет на операторите
    private static final Map<Character, Integer> precedence = new HashMap<>();
    static {
        precedence.put('*', 3);
        precedence.put('.', 2);
        precedence.put('|', 1);
    }

    // Добавяме изрични точки за конкатенация (напр. "ab" става "a.b")
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

    // Превръщане в Postfix (Обратен полски запис)
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