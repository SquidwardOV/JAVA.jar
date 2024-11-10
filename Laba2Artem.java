//pom.xml/////////////////////////////////////////////////////////////////////////////////////////

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>2</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>20</maven.compiler.source>
        <maven.compiler.target>20</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>RELEASE</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>5.7.0</version>
            <scope>test</scope>
        </dependency>

    </dependencies>

</project>

//ExpressionEvaluator.java///////////////////////////////////////////////////////////////////////////////

  package org.example;

import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

/**
 * Класс для проверки корректности записи математического выражения
 * и его вычисления с использованием обратной польской нотации (ОПЗ).
 */
public class ExpressionEvaluator {

    /**
     * Проверяет, корректно ли составлена формула.
     *
     * @param formula строка с выражением
     * @return true, если формула записана корректно; иначе false
     */
    public boolean checkFormula(String formula) {
        Stack<Character> bracketsStack = new Stack<>();
        boolean expectOperand = true;

        for (int i = 0; i < formula.length(); i++) {
            char c = formula.charAt(i);

            if (isSpace(c)) {
                continue;
            }

            if (isOpeningBracket(c)) {
                bracketsStack.push(c);
                expectOperand = true;
            } else if (isClosingBracket(c)) {
                if (bracketsStack.isEmpty() || !isMatchingBracket(bracketsStack.peek(), c)) {
                    return false;
                }
                bracketsStack.pop();
                expectOperand = false;
            } else if (isOperator(c)) {
                if (expectOperand) {
                    if (c == '-' && (i == 0 || formula.charAt(i - 1) == '(')) {
                        expectOperand = true;
                    } else {
                        return false;
                    }
                } else {
                    if (i + 1 < formula.length() && isOperator(formula.charAt(i + 1))) {
                        return false;
                    }
                    expectOperand = true;
                }
            } else if (Character.isDigit(c)) {  // Обработка цифр для более длинных чисел
                while (i < formula.length() && (Character.isDigit(formula.charAt(i)) || formula.charAt(i) == '.')) {
                    i++;  // Пропускаем всю последовательность цифр
                }
                i--;  // Возвращаемся на 1 позицию, так как внешний цикл также увеличивает i
                expectOperand = false;
            } else {
                return false;
            }
        }

        return bracketsStack.isEmpty() && !expectOperand;
    }


    /**
     * Оценивает выражение, если оно правильно составлено, используя обратную польскую нотацию.
     *
     * @param expression строка с выражением
     * @return результат вычисления
     */
    public double evaluateExpression(String expression) {
        List<String> rpn = convertToRPN(expression);  // Шаг 1: Преобразование в ОПЗ
        return calculateRPN(rpn);  // Шаг 2: Вычисление значения ОПЗ
    }

    /**
     * Преобразует инфиксное выражение в обратную польскую нотацию (ОПЗ).
     *
     * @param expression строка с выражением
     * @return список строк, представляющих выражение в ОПЗ
     */
    private List<String> convertToRPN(String expression) {
        List<String> output = new ArrayList<>();
        Stack<String> operators = new Stack<>();

        int i = 0;
        while (i < expression.length()) {
            char c = expression.charAt(i);

            if (Character.isDigit(c) || (c == '-' && (i == 0 || expression.charAt(i - 1) == '('))) {
                // Чтение числа (включая отрицательные)
                StringBuilder number = new StringBuilder();
                number.append(c);
                i++;

                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    number.append(expression.charAt(i));
                    i++;
                }
                output.add(number.toString());
                continue;
            } else if (c == '(') {
                operators.push(String.valueOf(c));
            } else if (c == ')') {
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    output.add(operators.pop());
                }
                operators.pop();
            } else if (isOperator(c)) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(String.valueOf(c))) {
                    output.add(operators.pop());
                }
                operators.push(String.valueOf(c));
            }
            i++;
        }

        while (!operators.isEmpty()) {
            output.add(operators.pop());
        }

        return output;
    }

    /**
     * Вычисляет значение выражения, записанного в ОПЗ.
     *
     * @param rpn список строк, представляющий выражение в ОПЗ
     * @return результат вычисления
     */
    private double calculateRPN(List<String> rpn) {
        Stack<Double> stack = new Stack<>();

        for (String token : rpn) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token.charAt(0))) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Некорректное выражение: недостаточно операндов для операции " + token);
                }
                double b = stack.pop();
                double a = stack.pop();
                switch (token) {
                    case "+":
                        stack.push(a + b);
                        break;
                    case "-":
                        stack.push(a - b);
                        break;
                    case "*":
                        stack.push(a * b);
                        break;
                    case "/":
                        if (b == 0) {
                            throw new ArithmeticException("Ошибка: деление на ноль");
                        }
                        stack.push(a / b);
                        break;
                    default:
                        throw new IllegalArgumentException("Неизвестный оператор: " + token);
                }
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Некорректное выражение: неверное количество элементов в стеке после вычисления.");
        }

        return stack.pop();
    }

    /**
     * Проверяет, является ли символ оператором.
     *
     * @param c символ для проверки
     * @return true, если символ является оператором; иначе false
     */
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    /**
     * Возвращает приоритет оператора.
     *
     * @param operator строка, представляющая оператор
     * @return целочисленное значение приоритета оператора
     */
    private int precedence(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            default:
                return 0;
        }
    }

    /**
     * Проверяет, является ли строка числом.
     *
     * @param token строка для проверки
     * @return true, если строка является числом; иначе false
     */
    private boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Проверяет, является ли символ открывающей скобкой.
     *
     * @param c символ для проверки
     * @return true, если символ является открывающей скобкой; иначе false
     */
    private boolean isOpeningBracket(char c) {
        return c == '(' || c == '[' || c == '{';
    }

    /**
     * Проверяет, является ли символ закрывающей скобкой.
     *
     * @param c символ для проверки
     * @return true, если символ является закрывающей скобкой; иначе false
     */
    private boolean isClosingBracket(char c) {
        return c == ')' || c == ']' || c == '}';
    }

    /**
     * Проверяет, соответствует ли пара открывающей и закрывающей скобок.
     *
     * @param opening символ открывающей скобки
     * @param closing символ закрывающей скобки
     * @return true, если скобки соответствуют друг другу; иначе false
     */
    private boolean isMatchingBracket(char opening, char closing) {
        return (opening == '(' && closing == ')') ||
                (opening == '[' && closing == ']') ||
                (opening == '{' && closing == '}');
    }

    /**
     * Проверяет, является ли символ операндом (буквой или цифрой).
     *
     * @param c символ для проверки
     * @return true, если символ является операндом; иначе false
     */
    private boolean isOperand(char c) {
        return Character.isLetterOrDigit(c);
    }

    /**
     * Проверяет, является ли символ пробелом.
     *
     * @param c символ для проверки
     * @return true, если символ является пробелом; иначе false
     */
    private boolean isSpace(char c) {
        return c == ' ';
    }
}

//main.java////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExpressionEvaluator evaluator = new ExpressionEvaluator();

        System.out.print("Введите выражение: ");
        String formula = scanner.nextLine();

        if (evaluator.checkFormula(formula)) {
            System.out.println("Формула записана верно!");
            double result = evaluator.evaluateExpression(formula);
            System.out.println("Результат вычисления выражения: " + result);
        } else {
            System.out.println("Формула записана неверно!");
        }

        scanner.close();
    }
}


//ExpressionEvaluatorTest.java//////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExpressionEvaluatorTest {

    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();

    /**
     * Тест для проверки корректности выражения с правильной записью.
     */
    @Test
    public void testCheckFormulaValid() {
        assertTrue(evaluator.checkFormula("502-53"), "Ожидается корректная формула");
        assertTrue(evaluator.checkFormula("10 + (20 - 5) * 3"), "Ожидается корректная формула");
        assertTrue(evaluator.checkFormula("3 + 4 * 2 / (1 - 5)"), "Ожидается корректная формула");
        assertTrue(evaluator.checkFormula("-5 + 3"), "Ожидается корректная формула с отрицательным числом");
    }

    /**
     * Тест для проверки корректности выражения с неправильной записью.
     */
    @Test
    public void testCheckFormulaInvalid() {
        assertFalse(evaluator.checkFormula("502-"), "Ожидается некорректная формула");
        assertFalse(evaluator.checkFormula("10 ++ 20"), "Ожидается некорректная формула с двойным оператором");
        assertFalse(evaluator.checkFormula("(5 + 3"), "Ожидается некорректная формула с несбалансированными скобками");
        assertFalse(evaluator.checkFormula("3 ** 4"), "Ожидается некорректная формула с недопустимыми операторами");
    }

    /**
     * Тест для вычисления простых выражений.
     */
    @Test
    public void testEvaluateExpressionSimple() {
        assertEquals(10, evaluator.evaluateExpression("5 + 5"), 0.0001, "Ожидается результат 10");
        assertEquals(25, evaluator.evaluateExpression("50 / 2"), 0.0001, "Ожидается результат 25");
        assertEquals(12, evaluator.evaluateExpression("3 * 4"), 0.0001, "Ожидается результат 12");
    }

    /**
     * Тест для вычисления выражений с отрицательными числами.
     */
    @Test
    public void testEvaluateExpressionWithNegativeNumbers() {
        assertEquals(-2, evaluator.evaluateExpression("3 - 5"), 0.0001, "Ожидается результат -2");
        assertEquals(-8, evaluator.evaluateExpression("-5 - 3"), 0.0001, "Ожидается результат -8");
        assertEquals(5, evaluator.evaluateExpression("-5 + 10"), 0.0001, "Ожидается результат 5");
    }

    /**
     * Тест для вычисления выражений с приоритетом операций.
     */
    @Test
    public void testEvaluateExpressionWithOperatorPrecedence() {
        assertEquals(10, evaluator.evaluateExpression("3 + 4 * 2 - 1"), 0.0001, "Ожидается результат 10");
        assertEquals(3, evaluator.evaluateExpression("(1 + 2) * (5 - 4)"), 0.0001, "Ожидается результат 3");
        assertEquals(1, evaluator.evaluateExpression("10 / (2 * 5)"), 0.0001, "Ожидается результат 1");
    }

    /**
     * Тест для проверки деления на ноль.
     */
    @Test
    public void testDivisionByZero() {
        assertThrows(ArithmeticException.class, () -> evaluator.evaluateExpression("5 / 0"), "Ожидается исключение деления на ноль");
    }

    /**
     * Тест для проверки некорректного выражения при вычислении.
     */
    @Test
    public void testInvalidExpressionForEvaluation() {
        assertThrows(IllegalArgumentException.class, () -> evaluator.evaluateExpression("3 + + 4"), "Ожидается исключение при некорректной формуле");
        assertThrows(IllegalArgumentException.class, () -> evaluator.evaluateExpression("5 * -"), "Ожидается исключение при некорректной формуле");
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
