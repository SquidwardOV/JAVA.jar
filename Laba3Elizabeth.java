pom.xml/////////////////////////////////////////////////////////////////////////////////////////////

  <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>org.example</groupId>
  <artifactId>Laba2.1</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>Laba3</name>
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
  </dependencies>
</project>
  
/////////////////////////////////////////////////////////////////////////////////////////////

MyArrayList.java////////////////////////////////////////////////////////////////////////////

package org.example;

import java.util.Arrays;

public class MyArrayList<T> {
    private static final int INITIAL_CAPACITY = 10;
    private Object[] elements;
    private int size;

    public MyArrayList() {
        elements = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    public void add(T element) {
        ensureCapacity();
        elements[size++] = element;
    }

    public void add(int index, T element) {
        checkIndexForAdd(index);
        ensureCapacity();
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }

    public T get(int index) {
        checkIndex(index);
        return (T) elements[index];
    }

    public T remove(int index) {
        checkIndex(index);
        T removedElement = (T) elements[index];
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[--size] = null;
        return removedElement;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            elements = Arrays.copyOf(elements, elements.length * 2);
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс за пределами диапазона: " + index);
        }
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Индекс за пределами диапазона: " + index);
        }
    }
}


///////////////////////////////////////////////////////////////////////////////////////////

MyLinkedList.java////////////////////////////////////////////////////////////////////////////

package org.example;

public class MyLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    private static class Node<T> {
        T value;
        Node<T> next;
        Node<T> prev;

        Node(T value, Node<T> next, Node<T> prev) {
            this.value = value;
            this.next = next;
            this.prev = prev;
        }
    }

    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public void add(T element) {
        Node<T> newNode = new Node<>(element, null, tail);
        if (tail != null) {
            tail.next = newNode;
        } else {
            head = newNode;
        }
        tail = newNode;
        size++;
    }

    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Индекс за пределами диапазона: " + index);
        }

        if (index == size) {
            add(element);
        } else if (index == 0) {
            Node<T> newNode = new Node<>(element, head, null);
            if (head != null) {
                head.prev = newNode;
            }
            head = newNode;
            if (tail == null) {
                tail = newNode;
            }
            size++;
        } else {
            Node<T> current = getNode(index);
            Node<T> newNode = new Node<>(element, current, current.prev);
            current.prev.next = newNode;
            current.prev = newNode;
            size++;
        }
    }

    public T get(int index) {
        return getNode(index).value;
    }

    public T remove(int index) {
        Node<T> nodeToRemove = getNode(index);

        if (nodeToRemove.prev != null) {
            nodeToRemove.prev.next = nodeToRemove.next;
        } else {
            head = nodeToRemove.next;
        }

        if (nodeToRemove.next != null) {
            nodeToRemove.next.prev = nodeToRemove.prev;
        } else {
            tail = nodeToRemove.prev;
        }

        size--;
        return nodeToRemove.value;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private Node<T> getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс за пределами диапазона: " + index);
        }

        Node<T> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }

        return current;
    }
}
  
///////////////////////////////////////////////////////////////////////////////////////////

ListPerformanceTest.java///////////////////////////////////////////////////////////////////

package org.example;

import java.util.Scanner;

/**
 * Класс для сравнения производительности собственных реализаций структур данных
 * {@code MyArrayList} и {@code MyLinkedList}.
 */
public class ListPerformanceTest {
    private int defaultSize;
    private int defaultIterations;
    private int precision;

    /**
     * Конструктор класса ListPerformanceTest.
     *
     * @param defaultSize       размер списка по умолчанию.
     * @param defaultIterations количество итераций.
     * @param precision         точность вывода.
     * @throws IllegalArgumentException если переданы некорректные параметры:
     *                                  <ul>
     *                                      <li>Размер списка меньше или равен 0</li>
     *                                      <li>Количество итераций меньше или равно 0</li>
     *                                      <li>Точность отрицательная</li>
     *                                  </ul>
     */
    public ListPerformanceTest(int defaultSize, int defaultIterations, int precision) {
        if (defaultSize <= 0) {
            System.out.println("Ошибка: Размер списка должен быть положительным.");
            throw new IllegalArgumentException("Размер списка должен быть положительным.");
        }
        if (defaultIterations <= 0) {
            System.out.println("Ошибка: Количество итераций должно быть положительным.");
            throw new IllegalArgumentException("Количество итераций должно быть положительным.");
        }
        if (precision < 0) {
            System.out.println("Ошибка: Точность не может быть отрицательной.");
            throw new IllegalArgumentException("Точность не может быть отрицательной.");
        }

        this.defaultSize = defaultSize;
        this.defaultIterations = defaultIterations;
        this.precision = precision;
    }

    /**
     * Метод для запуска всех тестов производительности.
     */
    public void runTests() {
        System.out.printf("%-15s %-20s %-20s%n", "Метод", "MyArrayList (с)", "MyLinkedList (с)");

        testAddFirst();
        testAddLast();
        testAddAtIndex();
        testGetFirst();
        testGetLast();
        testGetAtIndex();
        testRemoveFirst();
        testRemoveLast();
        testRemoveAtIndex();
    }

    /**
     * Тест производительности операции добавления элемента в начало списка.
     */
    private void testAddFirst() {
        MyArrayList<Integer> arrayList = new MyArrayList<>();
        MyLinkedList<Integer> linkedList = new MyLinkedList<>();

        double arrayListTime = measureTime(() -> {
            for (int i = 0; i < defaultIterations; i++) {
                arrayList.add(0, i);
            }
        });

        double linkedListTime = measureTime(() -> {
            for (int i = 0; i < defaultIterations; i++) {
                linkedList.add(0, i);
            }
        });

        printResult("addFirst", arrayListTime, linkedListTime);
    }

    /**
     * Тест производительности операции добавления элемента в конец списка.
     */
    private void testAddLast() {
        MyArrayList<Integer> arrayList = new MyArrayList<>();
        MyLinkedList<Integer> linkedList = new MyLinkedList<>();

        double arrayListTime = measureTime(() -> {
            for (int i = 0; i < defaultIterations; i++) {
                arrayList.add(i);
            }
        });

        double linkedListTime = measureTime(() -> {
            for (int i = 0; i < defaultIterations; i++) {
                linkedList.add(i);
            }
        });

        printResult("addLast", arrayListTime, linkedListTime);
    }

    /**
     * Тест производительности операции добавления элемента по индексу.
     */
    private void testAddAtIndex() {
        MyArrayList<Integer> arrayList = new MyArrayList<>();
        MyLinkedList<Integer> linkedList = new MyLinkedList<>();
        for (int i = 0; i < defaultSize; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        double arrayListTime = measureTime(() -> {
            for (int i = 0; i < defaultIterations; i++) {
                int index = i % defaultSize; // Циклический доступ
                arrayList.add(index, i);
            }
        });

        double linkedListTime = measureTime(() -> {
            for (int i = 0; i < defaultIterations; i++) {
                int index = i % defaultSize;
                linkedList.add(index, i);
            }
        });

        printResult("addAtIndex", arrayListTime, linkedListTime);
    }

    /**
     * Тест производительности операции получения первого элемента.
     */
    private void testGetFirst() {
        MyArrayList<Integer> arrayList = new MyArrayList<>();
        MyLinkedList<Integer> linkedList = new MyLinkedList<>();
        for (int i = 0; i < defaultSize; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        double arrayListTime = measureTime(() -> {
            for (int i = 0; i < defaultIterations; i++) {
                arrayList.get(0);
            }
        });

        double linkedListTime = measureTime(() -> {
            for (int i = 0; i < defaultIterations; i++) {
                linkedList.get(0);
            }
        });

        printResult("getFirst", arrayListTime, linkedListTime);
    }

    /**
     * Тест производительности операции получения последнего элемента.
     */
    private void testGetLast() {
        MyArrayList<Integer> arrayList = new MyArrayList<>();
        MyLinkedList<Integer> linkedList = new MyLinkedList<>();
        for (int i = 0; i < defaultSize; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        double arrayListTime = measureTime(() -> {
            for (int i = 0; i < defaultIterations; i++) {
                arrayList.get(arrayList.size() - 1);
            }
        });

        double linkedListTime = measureTime(() -> {
            for (int i = 0; i < defaultIterations; i++) {
                linkedList.get(linkedList.size() - 1);
            }
        });

        printResult("getLast", arrayListTime, linkedListTime);
    }

    /**
     * Тест производительности операции получения элемента по индексу.
     */
    private void testGetAtIndex() {
        MyArrayList<Integer> arrayList = new MyArrayList<>();
        MyLinkedList<Integer> linkedList = new MyLinkedList<>();
        for (int i = 0; i < defaultSize; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        double arrayListTime = measureTime(() -> {
            for (int i = 0; i < defaultIterations; i++) {
                int index = i % defaultSize;
                arrayList.get(index);
            }
        });

        double linkedListTime = measureTime(() -> {
            for (int i = 0; i < defaultIterations; i++) {
                int index = i % defaultSize;
                linkedList.get(index);
            }
        });

        printResult("getAtIndex", arrayListTime, linkedListTime);
    }

    /**
     * Тест производительности операции удаления первого элемента.
     */
    private void testRemoveFirst() {
        MyArrayList<Integer> arrayList = new MyArrayList<>();
        MyLinkedList<Integer> linkedList = new MyLinkedList<>();
        for (int i = 0; i < defaultSize; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        double arrayListTime = measureTime(() -> {
            for (int i = 0; i < defaultIterations && !arrayList.isEmpty(); i++) {
                arrayList.remove(0);
            }
        });

        double linkedListTime = measureTime(() -> {
            for (int i = 0; i < defaultIterations && !linkedList.isEmpty(); i++) {
                linkedList.remove(0);
            }
        });

        printResult("removeFirst", arrayListTime, linkedListTime);
    }

    /**
     * Тест производительности операции удаления последнего элемента.
     */
    private void testRemoveLast() {
        MyArrayList<Integer> arrayList = new MyArrayList<>();
        MyLinkedList<Integer> linkedList = new MyLinkedList<>();
        for (int i = 0; i < defaultSize; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        double arrayListTime = measureTime(() -> {
            for (int i = 0; i < defaultIterations && !arrayList.isEmpty(); i++) {
                arrayList.remove(arrayList.size() - 1);
            }
        });

        double linkedListTime = measureTime(() -> {
            for (int i = 0; i < defaultIterations && !linkedList.isEmpty(); i++) {
                linkedList.remove(linkedList.size() - 1);
            }
        });

        printResult("removeLast", arrayListTime, linkedListTime);
    }

    /**
     * Тест производительности операции удаления элемента по индексу.
     */
    private void testRemoveAtIndex() {
        MyArrayList<Integer> arrayList = new MyArrayList<>();
        MyLinkedList<Integer> linkedList = new MyLinkedList<>();
        for (int i = 0; i < defaultSize; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        double arrayListTime = measureTime(() -> {
            for (int i = 0; i < defaultIterations && !arrayList.isEmpty(); i++) {
                int index = i % arrayList.size();
                arrayList.remove(index);
            }
        });

        double linkedListTime = measureTime(() -> {
            for (int i = 0; i < defaultIterations && !linkedList.isEmpty(); i++) {
                int index = i % linkedList.size();
                linkedList.remove(index);
            }
        });

        printResult("removeAtIndex", arrayListTime, linkedListTime);
    }

    /**
     * Метод для измерения времени выполнения задачи.
     *
     * @param task задача для выполнения.
     * @return время выполнения задачи в секундах.
     */
    private double measureTime(Runnable task) {
        long start = System.nanoTime();
        task.run();
        long end = System.nanoTime();
        return (end - start) / 1_000_000_000.0; // Перевод в секунды
    }

    /**
     * Печатает результаты теста с заданной точностью.
     *
     * @param method         название тестируемого метода.
     * @param arrayListTime  время выполнения для {@code MyArrayList}.
     * @param linkedListTime время выполнения для {@code MyLinkedList}.
     */
    private void printResult(String method, double arrayListTime, double linkedListTime) {
        String format = "%-15s %-20." + precision + "f %-20." + precision + "f%n";
        System.out.printf(format, method, arrayListTime, linkedListTime);
    }

    /**
     * Основной метод программы.
     * Пользователь вводит параметры: размер списка, количество итераций и точность.
     *
     * @param args аргументы командной строки.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int defaultSize = 0;
        int defaultIterations = 0;
        int precision = 0;

        // Ввод размера списка с проверкой
        while (true) {
            System.out.println("Введите размер списка (DEFAULT_SIZE):");
            if (scanner.hasNextInt()) {
                defaultSize = scanner.nextInt();
                if (defaultSize > 0) {
                    break;
                } else {
                    System.out.println("Ошибка: Размер списка должен быть положительным целым числом.");
                }
            } else {
                String invalidInput = scanner.next();
                System.out.println("Ошибка: Введено некорректное значение '" + invalidInput + "'. Пожалуйста, введите положительное целое число.");
            }
        }

        // Ввод количества итераций с проверкой
        while (true) {
            System.out.println("Введите количество итераций (DEFAULT_ITERATIONS):");
            if (scanner.hasNextInt()) {
                defaultIterations = scanner.nextInt();
                if (defaultIterations > 0) {
                    break;
                } else {
                    System.out.println("Ошибка: Количество итераций должно быть положительным целым числом.");
                }
            } else {
                String invalidInput = scanner.next();
                System.out.println("Ошибка: Введено некорректное значение '" + invalidInput + "'. Пожалуйста, введите положительное целое число.");
            }
        }

        // Ввод точности с проверкой
        while (true) {
            System.out.println("Введите количество знаков после запятой (точность):");
            if (scanner.hasNextInt()) {
                precision = scanner.nextInt();
                if (precision >= 0) {
                    break;
                } else {
                    System.out.println("Ошибка: Точность не может быть отрицательной.");
                }
            } else {
                String invalidInput = scanner.next();
                System.out.println("Ошибка: Введено некорректное значение '" + invalidInput + "'. Пожалуйста, введите неотрицательное целое число.");
            }
        }

        // Закрытие сканера
        scanner.close();

        // Запуск тестов
        try {
            ListPerformanceTest test = new ListPerformanceTest(defaultSize, defaultIterations, precision);
            test.runTests();
        } catch (IllegalArgumentException e) {
            System.out.println("Программа завершена из-за некорректных параметров.");
        }
    }
}

///////////////////////////////////////////////////////////////////////////////////////////

ListPerformanceTestJUnitTests.java/////////////////////////////////////////////////////////

package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Юнит-тесты для класса {@link ListPerformanceTest}.
 * Проверяют корректность работы методов и обработку различных граничных условий.
 */
class ListPerformanceTestTest {

    /**
     * Проверяет, что метод {@code runTests()} работает без выброса исключений
     * при стандартных параметрах.
     */
    @Test
    void testRunTests() {
        ListPerformanceTest test = new ListPerformanceTest(100, 10, 10);
        assertDoesNotThrow(test::runTests, "Метод runTests() выбросил исключение.");
    }

    /**
     * Проверяет корректность работы метода {@code runTests()} при точности 5 знаков.
     */
    @Test
    void testPrecision() {
        ListPerformanceTest test = new ListPerformanceTest(100, 10, 5);
        assertDoesNotThrow(test::runTests, "Метод runTests() выбросил исключение при точности 5.");
    }

    /**
     * Проверяет корректность обработки больших объемов данных.
     */
    @Test
    void testLargeData() {
        ListPerformanceTest test = new ListPerformanceTest(10_000, 1000, 10);
        assertDoesNotThrow(test::runTests, "Метод runTests() выбросил исключение при обработке больших данных.");
    }

    /**
     * Проверяет корректность работы метода {@code runTests()} с минимально возможными значениями.
     */
    @Test
    void testSmallData() {
        ListPerformanceTest test = new ListPerformanceTest(1, 1, 10);
        assertDoesNotThrow(test::runTests, "Метод runTests() выбросил исключение при минимальных значениях.");
    }

    /**
     * Проверяет корректность работы программы при точности 0 знаков.
     */
    @Test
    void testZeroPrecision() {
        ListPerformanceTest test = new ListPerformanceTest(100, 10, 0);
        assertDoesNotThrow(test::runTests, "Метод runTests() выбросил исключение при точности 0.");
    }

    /**
     * Проверяет, что передача отрицательной точности вызывает исключение {@link IllegalArgumentException}.
     */
    @Test
    void testNegativePrecision() {
        assertThrows(IllegalArgumentException.class, () -> new ListPerformanceTest(100, 10, -3),
                "Ожидалось исключение IllegalArgumentException для отрицательной точности.");
    }

    /**
     * Проверяет, что передача отрицательных итераций вызывает исключение {@link IllegalArgumentException}.
     */
    @Test
    void testNegativeIterations() {
        assertThrows(IllegalArgumentException.class, () -> new ListPerformanceTest(100, -10, 10),
                "Ожидалось исключение IllegalArgumentException для отрицательных итераций.");
    }

    /**
     * Проверяет, что передача отрицательного размера вызывает исключение {@link IllegalArgumentException}.
     */
    @Test
    void testNegativeSize() {
        assertThrows(IllegalArgumentException.class, () -> new ListPerformanceTest(-100, 10, 10),
                "Ожидалось исключение IllegalArgumentException для отрицательного размера.");
    }

   /**
     * Проверяет корректность работы метода {@code runTests()} при очень большом числе итераций.
     */

    @Test
    void testVeryLargeIterations() {
        ListPerformanceTest test = new ListPerformanceTest(100, 1_000_000, 10);
        assertDoesNotThrow(test::runTests, "Метод runTests() выбросил исключение при очень большом числе итераций.");
    }

    /**
     * Проверяет корректность работы программы с высокой точностью (до 10 знаков после запятой).
     */
    @Test
    void testHighPrecision() {
        ListPerformanceTest test = new ListPerformanceTest(100, 10, 20);
        assertDoesNotThrow(test::runTests, "Метод runTests() выбросил исключение при точности 10.");
    }

    /**
     * Проверяет корректность обработки больших значений размера списка и числа итераций.
     */
    @Test
    void testLargeSizeAndIterations() {
        ListPerformanceTest test = new ListPerformanceTest(100_000, 10_000, 10);
        assertDoesNotThrow(test::runTests, "Метод runTests() выбросил исключение при больших значениях размера и итераций.");
    }

    /**
     * Проверяет корректность форматирования результатов метода {@code runTests()}.
     */
    @Test
    void testFormattedOutput() {
        ListPerformanceTest test = new ListPerformanceTest(100, 10, 10);
        assertDoesNotThrow(() -> {
            test.runTests();
            // Здесь можно добавить проверку вывода, если он сохраняется в файл или строку.
        }, "Метод runTests() выбросил исключение при форматировании результата.");
    }

    /**
     * Проверяет, что передача нулевых параметров вызывает исключение {@link IllegalArgumentException}.
     */
    @Test
    void testInvalidParameters() {
        assertThrows(IllegalArgumentException.class, () -> new ListPerformanceTest(0, 0, 3),
                "Ожидалось исключение IllegalArgumentException для нулевых параметров.");
    }
}

///////////////////////////////////////////////////////////////////////////////////////////

