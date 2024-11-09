//pom.xml

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>org.example</groupId>
  <artifactId>Laba2Lizochka</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>Laba2Lizochka</name>
  <url>http://maven.apache.org</url>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
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

  ///////////////////////////////////ExpressionParser.java////////////////////////////////////

  package org.example;

import java.util.*; // Импортируем пакет java.util для работы с коллекциями и утилитами
import java.util.regex.Matcher; // Импортируем класс Matcher для работы с регулярными выражениями
import java.util.regex.Pattern; // Импортируем класс Pattern для создания шаблонов регулярных выражений

/**
 * Класс ExpressionParser позволяет вычислять выражения с поддержкой
 * переменных и стандартных математических функций.
 */
public class ExpressionParser {

    /** Шаблон для поиска переменных в выражении: последовательности букв */
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\b[a-zA-Z]+\\b");

    /** Множество поддерживаемых математических функций */
    private static final Set<String> FUNCTIONS = new HashSet<>(Arrays.asList("sin", "cos", "tan", "log", "sqrt"));

    /**
     * Точка входа в программу. Запрашивает выражение и значения переменных у пользователя,
     * вычисляет результат и выводит его.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Создаем объект Scanner для ввода данных от пользователя
        System.out.print("Enter an expression: "); // Запрашиваем ввод выражения
        String expression = scanner.nextLine(); // Считываем введенное выражение

        Map<String, Double> variableValues = new HashMap<>(); // Map для хранения значений переменных
        Matcher matcher = VARIABLE_PATTERN.matcher(expression); // Инициализируем Matcher для поиска переменных в выражении

        // Проходим по выражению и находим все переменные
        while (matcher.find()) {
            String variable = matcher.group(); // Получаем найденную переменную
            if (!FUNCTIONS.contains(variable)) { // Проверяем, что переменная не является функцией
                if (!variableValues.containsKey(variable)) { // Если переменная еще не добавлена в Map
                    System.out.print("Enter the value for variable " + variable + ": "); // Запрашиваем значение переменной у пользователя
                    variableValues.put(variable, scanner.nextDouble()); // Сохраняем значение переменной
                }
            }
        }

        try {
            double result = evaluate(expression, variableValues); // Вычисляем результат выражения
            System.out.println("Result: " + result); // Выводим результат
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage()); // Обрабатываем исключение, если оно возникло, и выводим сообщение об ошибке
        }
    }

    /**
     * Вычисляет выражение, заменяя переменные их значениями.
     *
     * @param expression строка с математическим выражением
     * @param variableValues значения переменных
     * @return результат вычисления выражения
     * @throws RuntimeException если выражение некорректное
     */
    public static double evaluate(String expression, Map<String, Double> variableValues) {
        if (!isValidExpression(expression)) { // Проверяем, является ли выражение валидным
            throw new RuntimeException("Invalid expression syntax"); // Выбрасываем RuntimeException для общего синтаксиса
        }

        try {
            expression = replaceVariables(expression, variableValues); // Заменяем переменные на их значения
            return evaluateExpression(expression); // Вычисляем выражение
        } catch (RuntimeException e) {
            throw e; // Пробрасываем специфичные RuntimeException без изменения
        } catch (Exception e) {
            throw new RuntimeException("Invalid expression syntax"); // Преобразуем общее исключение в RuntimeException
        }
    }

    /**
     * Заменяет переменные в выражении на их значения.
     *
     * @param expression строка с математическим выражением
     * @param variableValues значения переменных
     * @return выражение, в котором переменные заменены на значения
     */
    public static String replaceVariables(String expression, Map<String, Double> variableValues) {
        for (Map.Entry<String, Double> entry : variableValues.entrySet()) {
            expression = expression.replaceAll("\\b" + entry.getKey() + "\\b", entry.getValue().toString()); // Заменяем каждое вхождение переменной на ее значение
        }
        return expression; // Возвращаем обновленное выражение
    }

    /**
     * Проверяет, является ли выражение валидным.
     *
     * @param expression строка с математическим выражением
     * @return true, если выражение валидное, иначе false
     */
    private static boolean isValidExpression(String expression) {
        // Проверяем на наличие недопустимых последовательностей (например, двойных операторов)
        Pattern invalidPattern = Pattern.compile("([+\\-*/^]{2,})|([+\\-*/^]$)|(^[+\\-*/^])");
        Matcher matcher = invalidPattern.matcher(expression);
        return !matcher.find(); // Возвращаем false, если найден недопустимый шаблон
    }

    /**
     * Вычисляет математическое выражение, поддерживающее операции сложения,
     * вычитания, умножения, деления, возведения в степень и функции.
     *
     * @param expression строка с математическим выражением
     * @return результат вычисления выражения
     */
    public static double evaluateExpression(String expression) {
        return new Object() { // Анонимный класс для анализа и вычисления выражения
            int pos = -1, ch; // Позиция и текущий символ в выражении

            /**
             * Переходит к следующему символу в выражении.
             */
            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1; // Если не конец выражения, считываем следующий символ, иначе -1
            }

            /**
             * Проверяет, совпадает ли текущий символ с ожидаемым, и переходит к следующему символу, если совпадает.
             *
             * @param charToEat ожидаемый символ
             * @return true, если символ совпадает, иначе false
             */
            boolean eat(int charToEat) {
                while (ch == ' ') nextChar(); // Пропускаем пробелы
                if (ch == charToEat) { // Если символ совпадает
                    nextChar(); // Переходим к следующему символу
                    return true;
                }
                return false; // Иначе возвращаем false
            }

            /**
             * Выполняет разбор и вычисление выражения.
             *
             * @return результат вычисления выражения
             */
            double parse() {
                nextChar(); // Начинаем с первого символа
                double x = parseExpression(); // Вычисляем значение выражения
                if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char) ch); // Если остались неразобранные символы, выбрасываем ошибку
                return x; // Возвращаем результат
            }

            /**
             * Разбирает и вычисляет выражение с операциями сложения и вычитания.
             *
             * @return результат вычисления выражения
             */
            double parseExpression() {
                double x = parseTerm(); // Вычисляем первый член
                for (;;) {
                    if (eat('+')) x += parseTerm(); // Сложение
                    else if (eat('-')) x -= parseTerm(); // Вычитание
                    else return x; // Возвращаем результат
                }
            }

            /**
             * Разбирает и вычисляет выражение с операциями умножения и деления.
             *
             * @return результат вычисления выражения
             */
            double parseTerm() {
                double x = parseFactor(); // Вычисляем первый множитель
                for (;;) {
                    if (eat('*')) x *= parseFactor(); // Умножение
                    else if (eat('/')) x /= parseFactor(); // Деление
                    else return x; // Возвращаем результат
                }
            }

            /**
             * Разбирает и вычисляет числа, функции, скобки и унарные операторы.
             *
             * @return результат вычисления выражения
             */
            double parseFactor() {
                if (eat('+')) return parseFactor(); // Унарный плюс
                if (eat('-')) return -parseFactor(); // Унарный минус

                double x;
                int startPos = this.pos;
                if (eat('(')) { // Если открывающая скобка
                    x = parseExpression(); // Вычисляем выражение в скобках
                    if (!eat(')')) throw new RuntimeException("Mismatched parentheses"); // Проверяем, закрыта ли скобка
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // Если это число
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar(); // Считываем все цифры и точки
                    x = Double.parseDouble(expression.substring(startPos, this.pos)); // Преобразуем строку в число
                } else if (ch >= 'a' && ch <= 'z') { // Если это функция
                    while (ch >= 'a' && ch <= 'z') nextChar(); // Считываем название функции
                    String func = expression.substring(startPos, this.pos); // Получаем имя функции
                    x = parseFactor(); // Вычисляем аргумент функции
                    x = applyFunction(func, x); // Применяем функцию
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch); // Неожиданный символ
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // Возведение в степень

                return x; // Возвращаем результат
            }

            /**
             * Применяет математическую функцию к аргументу.
             *
             * @param func название функции
             * @param arg значение аргумента
             * @return результат применения функции
             * @throws RuntimeException если функция неизвестна
             */
            double applyFunction(String func, double arg) {
                switch (func) {
                    case "sqrt": return Math.sqrt(arg); // Квадратный корень
                    case "sin": return Math.sin(Math.toRadians(arg)); // Синус
                    case "cos": return Math.cos(Math.toRadians(arg)); // Косинус
                    case "tan": return Math.tan(Math.toRadians(arg)); // Тангенс
                    case "log": return Math.log(arg); // Логарифм
                    default: throw new RuntimeException("Unknown function: " + func); // Неизвестная функция
                }
            }
        }.parse(); // Начинаем парсинг с вызова метода parse()
    }
}

///ExpressionParserTest.java////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;


public class ExpressionParserTest {

    @Test
    public void testSimpleAddition() throws Exception {
        assertEquals(5.0, ExpressionParser.evaluate("2+3", new HashMap<>()), 0.001);
    }

    @Test
    public void testSimpleSubtraction() throws Exception {
        assertEquals(2.0, ExpressionParser.evaluate("5-3", new HashMap<>()), 0.001);
    }

    @Test
    public void testMultiplication() throws Exception {
        assertEquals(15.0, ExpressionParser.evaluate("5*3", new HashMap<>()), 0.001);
    }

    @Test
    public void testDivision() throws Exception {
        assertEquals(2.0, ExpressionParser.evaluate("6/3", new HashMap<>()), 0.001);
    }

    @Test
    public void testExponentiation() throws Exception {
        assertEquals(8.0, ExpressionParser.evaluate("2^3", new HashMap<>()), 0.001);
    }

    @Test
    public void testVariableSubstitution() throws Exception {
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 4.0);
        assertEquals(7.0, ExpressionParser.evaluate("x+3", variables), 0.001);
    }

    @Test
    public void testMultipleVariableSubstitution() throws Exception {
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 4.0);
        variables.put("y", 5.0);
        assertEquals(20.0, ExpressionParser.evaluate("x*y", variables), 0.001);
    }

    @Test
    public void testFunctionSine() throws Exception {
        assertEquals(0.0, ExpressionParser.evaluate("sin(0)", new HashMap<>()), 0.001);
    }

    @Test
    public void testFunctionCosine() throws Exception {
        assertEquals(1.0, ExpressionParser.evaluate("cos(0)", new HashMap<>()), 0.001);
    }

    @Test
    public void testFunctionSquareRoot() throws Exception {
        assertEquals(3.0, ExpressionParser.evaluate("sqrt(9)", new HashMap<>()), 0.001);
    }

    @Test
    public void testComplexExpression() throws Exception {
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 2.0);
        assertEquals(10.0, ExpressionParser.evaluate("2*x+6", variables), 0.001);
    }

    @Test
    public void testParentheses() throws Exception {
        assertEquals(14.0, ExpressionParser.evaluate("2*(3+4)", new HashMap<>()), 0.001);
    }

    @Test
    public void testMixedOperations() throws Exception {
        assertEquals(8.0, ExpressionParser.evaluate("3+4*2-3", new HashMap<>()), 0.001);
    }

    @Test
    public void testInvalidExpression() {
        Exception exception = assertThrows(Exception.class, () -> {
            ExpressionParser.evaluate("2++3", new HashMap<>());
        });
        assertEquals("Invalid expression syntax", exception.getMessage());
    }

    @Test
    public void testUnknownFunction() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            ExpressionParser.evaluate("unknown(2)", new HashMap<>());
        });
        assertEquals("Unknown function: unknown", exception.getMessage());
    }


    @Test
    public void testMismatchedParentheses() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            ExpressionParser.evaluate("(3+4", new HashMap<>());
        });
        assertEquals("Mismatched parentheses", exception.getMessage());
    }
}

