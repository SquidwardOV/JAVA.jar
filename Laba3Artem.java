pom.xml//////////////////////////////////////////////////////////////////////////////////////////

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>org.example</groupId>
  <artifactId>untitled</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>untitled</name>
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

  
//////////////////////////////////////////////////////////////////////////////////////////

MyArrayList.java/////////////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

import java.util.Arrays;

/**
 * Ручная реализация динамического массива, аналогичного ArrayList.
 *
 * @param <E> Тип элементов, хранящихся в списке.
 */
public class MyArrayList<E> {
    private Object[] elements;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Конструктор по умолчанию.
     */
    public MyArrayList() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    /**
     * Добавляет элемент в конец списка.
     *
     * @param e Элемент для добавления.
     */
    public void add(E e) {
        ensureCapacity(size + 1);
        elements[size++] = e;
    }

    /**
     * Добавляет элемент по указанному индексу.
     *
     * @param index Индекс, по которому добавляется элемент.
     * @param e     Элемент для добавления.
     * @throws IndexOutOfBoundsException Если индекс некорректен.
     */
    public void add(int index, E e) {
        checkPositionIndex(index);
        ensureCapacity(size + 1);
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = e;
        size++;
    }

    /**
     * Удаляет элемент по индексу.
     *
     * @param index Индекс удаляемого элемента.
     * @return Удаленный элемент.
     * @throws IndexOutOfBoundsException Если индекс некорректен.
     */
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        checkElementIndex(index);
        E removedElement = (E) elements[index];
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        elements[--size] = null; // Удаляем ссылку для GC
        return removedElement;
    }

    /**
     * Возвращает элемент по указанному индексу.
     *
     * @param index Индекс элемента.
     * @return Элемент.
     * @throws IndexOutOfBoundsException Если индекс некорректен.
     */
    @SuppressWarnings("unchecked")
    public E get(int index) {
        checkElementIndex(index);
        return (E) elements[index];
    }

    /**
     * Возвращает размер списка.
     *
     * @return Количество элементов.
     */
    public int size() {
        return size;
    }

    /**
     * Обеспечивает достаточную емкость массива.
     *
     * @param minCapacity Минимальная требуемая емкость.
     */
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length * 2;
            if (newCapacity < minCapacity)
                newCapacity = minCapacity;
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }

    /**
     * Проверяет, находится ли индекс в пределах допустимых границ для добавления.
     *
     * @param index Индекс.
     * @throws IndexOutOfBoundsException Если индекс некорректен.
     */
    private void checkPositionIndex(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Индекс: " + index + ", размер: " + size);
    }

    /**
     * Проверяет, находится ли индекс в пределах допустимых границ для доступа.
     *
     * @param index Индекс.
     * @throws IndexOutOfBoundsException Если индекс некорректен.
     */
    private void checkElementIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Индекс: " + index + ", размер: " + size);
    }

    @Override
    public String toString() {
        if (size == 0)
            return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for(int i=0; i<size; i++) {
            sb.append(elements[i]);
            if(i != size -1)
                sb.append(", ");
        }
        sb.append(']');
        return sb.toString();
    }
}

//////////////////////////////////////////////////////////////////////////////////////////

MyLinkedList.java/////////////////////////////////////////////////////////////////////////////////////////////////////////////  
  
package org.example;

/**
 * Ручная реализация двусвязного списка, аналогичного LinkedList.
 *
 * @param <E> Тип элементов, хранящихся в списке.
 */
public class MyLinkedList<E> {
    /**
     * Внутренний узел списка.
     */
    private static class Node<E> {
        E data;
        Node<E> prev;
        Node<E> next;

        Node(E data) {
            this.data = data;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    /**
     * Конструктор по умолчанию.
     */
    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Добавляет элемент в конец списка.
     *
     * @param e Элемент для добавления.
     */
    public void add(E e) {
        Node<E> newNode = new Node<>(e);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    /**
     * Добавляет элемент по указанному индексу.
     *
     * @param index Индекс, по которому добавляется элемент.
     * @param e     Элемент для добавления.
     * @throws IndexOutOfBoundsException Если индекс некорректен.
     */
    public void add(int index, E e) {
        checkPositionIndex(index);
        if (index == size) {
            add(e);
            return;
        }
        Node<E> current = getNode(index);
        Node<E> newNode = new Node<>(e);
        newNode.next = current;
        newNode.prev = current.prev;
        if (current.prev != null)
            current.prev.next = newNode;
        else
            head = newNode;
        current.prev = newNode;
        size++;
    }

    /**
     * Удаляет элемент по индексу.
     *
     * @param index Индекс удаляемого элемента.
     * @return Удаленный элемент.
     * @throws IndexOutOfBoundsException Если индекс некорректен.
     */
    public E remove(int index) {
        checkElementIndex(index);
        Node<E> current = getNode(index);
        E removedData = current.data;
        if (current.prev != null)
            current.prev.next = current.next;
        else
            head = current.next;
        if (current.next != null)
            current.next.prev = current.prev;
        else
            tail = current.prev;
        size--;
        return removedData;
    }

    /**
     * Возвращает элемент по указанному индексу.
     *
     * @param index Индекс элемента.
     * @return Элемент.
     * @throws IndexOutOfBoundsException Если индекс некорректен.
     */
    public E get(int index) {
        checkElementIndex(index);
        return getNode(index).data;
    }

    /**
     * Возвращает размер списка.
     *
     * @return Количество элементов.
     */
    public int size() {
        return size;
    }

    /**
     * Получает узел по индексу.
     *
     * @param index Индекс.
     * @return Узел.
     */
    private Node<E> getNode(int index) {
        if (index < (size >> 1)) {
            Node<E> x = head;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            Node<E> x = tail;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }

    /**
     * Проверяет, находится ли индекс в пределах допустимых границ для добавления.
     *
     * @param index Индекс.
     * @throws IndexOutOfBoundsException Если индекс некорректен.
     */
    private void checkPositionIndex(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Индекс: " + index + ", размер: " + size);
    }

    /**
     * Проверяет, находится ли индекс в пределах допустимых границ для доступа.
     *
     * @param index Индекс.
     * @throws IndexOutOfBoundsException Если индекс некорректен.
     */
    private void checkElementIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Индекс: " + index + ", размер: " + size);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<E> current = head;
        while(current != null) {
            sb.append(current.data);
            if(current.next != null)
                sb.append(", ");
            current = current.next;
        }
        sb.append(']');
        return sb.toString();
    }
}

//////////////////////////////////////////////////////////////////////////////////////////


PerformanceTest.java/////////////////////////////////////////////////////////////////////////////////////////////////////////
  
package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Класс для тестирования производительности MyArrayList и MyLinkedList.
 */
public class PerformanceTest {
    /**
     * Основной метод программы.
     *
     * @param args Аргументы командной строки.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int operations = 0;
        int listSize = 0;
        int decimalPlaces = 3;

        try {
            System.out.print("Введите количество операций (например, 1000): ");
            operations = getPositiveInt(scanner);

            System.out.print("Введите начальный размер списка: ");
            listSize = getPositiveInt(scanner);

            System.out.print("Введите количество знаков после запятой для вывода времени: ");
            decimalPlaces = getNonNegativeInt(scanner);
        } catch (InputMismatchException e) {
            System.out.println("Некорректный ввод. Программа завершена.");
            scanner.close();
            return;
        }

        // Инициализация списков
        MyArrayList<Integer> myArrayList = new MyArrayList<>();
        MyLinkedList<Integer> myLinkedList = new MyLinkedList<>();

        // Заполнение списков начальными элементами
        for(int i=0; i<listSize; i++) {
            myArrayList.add(i);
            myLinkedList.add(i);
        }

        // Тестирование методов
        System.out.println("\nТестирование MyArrayList:");
        performTests(myArrayList, operations, decimalPlaces);

        System.out.println("\nТестирование MyLinkedList:");
        performTests(myLinkedList, operations, decimalPlaces);

        scanner.close();
    }

    /**
     * Выполняет тестирование основных методов коллекции.
     *
     * @param list         Коллекция для тестирования.
     * @param operations   Количество операций.
     * @param decimalPlaces Количество знаков после запятой для времени.
     */
    protected static void performTests(Object list, int operations, int decimalPlaces) {
        // Добавление в начало
        long start = System.nanoTime();
        if(list instanceof MyArrayList) {
            MyArrayList<Integer> arrayList = (MyArrayList<Integer>) list;
            for(int i=0; i<operations; i++) {
                arrayList.add(0, i);
            }
        } else if(list instanceof MyLinkedList) {
            MyLinkedList<Integer> linkedList = (MyLinkedList<Integer>) list;
            for(int i=0; i<operations; i++) {
                linkedList.add(0, i);
            }
        }
        long end = System.nanoTime();
        printResult("Добавление в начало", operations, end - start, decimalPlaces);

        // Добавление в конец
        start = System.nanoTime();
        if(list instanceof MyArrayList) {
            MyArrayList<Integer> arrayList = (MyArrayList<Integer>) list;
            for(int i=0; i<operations; i++) {
                arrayList.add(i);
            }
        } else if(list instanceof MyLinkedList) {
            MyLinkedList<Integer> linkedList = (MyLinkedList<Integer>) list;
            for(int i=0; i<operations; i++) {
                linkedList.add(i);
            }
        }
        end = System.nanoTime();
        printResult("Добавление в конец", operations, end - start, decimalPlaces);

        // Добавление по произвольному индексу (середина)
        int middleIndex = list instanceof MyArrayList ? ((MyArrayList<Integer>) list).size() / 2 : ((MyLinkedList<Integer>) list).size() / 2;
        start = System.nanoTime();
        if(list instanceof MyArrayList) {
            MyArrayList<Integer> arrayList = (MyArrayList<Integer>) list;
            for(int i=0; i<operations; i++) {
                arrayList.add(middleIndex, i);
            }
        } else if(list instanceof MyLinkedList) {
            MyLinkedList<Integer> linkedList = (MyLinkedList<Integer>) list;
            for(int i=0; i<operations; i++) {
                linkedList.add(middleIndex, i);
            }
        }
        end = System.nanoTime();
        printResult("Добавление по индексу", operations, end - start, decimalPlaces);

        // Удаление из начала
        start = System.nanoTime();
        if(list instanceof MyArrayList) {
            MyArrayList<Integer> arrayList = (MyArrayList<Integer>) list;
            for(int i=0; i<operations; i++) {
                if(arrayList.size() > 0)
                    arrayList.remove(0);
            }
        } else if(list instanceof MyLinkedList) {
            MyLinkedList<Integer> linkedList = (MyLinkedList<Integer>) list;
            for(int i=0; i<operations; i++) {
                if(linkedList.size() > 0)
                    linkedList.remove(0);
            }
        }
        end = System.nanoTime();
        printResult("Удаление из начала", operations, end - start, decimalPlaces);

        // Удаление из конца
        start = System.nanoTime();
        if(list instanceof MyArrayList) {
            MyArrayList<Integer> arrayList = (MyArrayList<Integer>) list;
            for(int i=0; i<operations; i++) {
                if(arrayList.size() > 0)
                    arrayList.remove(arrayList.size() - 1);
            }
        } else if(list instanceof MyLinkedList) {
            MyLinkedList<Integer> linkedList = (MyLinkedList<Integer>) list;
            for(int i=0; i<operations; i++) {
                if(linkedList.size() > 0)
                    linkedList.remove(linkedList.size() - 1);
            }
        }
        end = System.nanoTime();
        printResult("Удаление из конца", operations, end - start, decimalPlaces);

        // Удаление из середины
        start = System.nanoTime();
        if(list instanceof MyArrayList) {
            MyArrayList<Integer> arrayList = (MyArrayList<Integer>) list;
            for(int i=0; i<operations; i++) {
                int idx = arrayList.size() / 2;
                if(arrayList.size() > idx)
                    arrayList.remove(idx);
            }
        } else if(list instanceof MyLinkedList) {
            MyLinkedList<Integer> linkedList = (MyLinkedList<Integer>) list;
            for(int i=0; i<operations; i++) {
                int idx = linkedList.size() / 2;
                if(linkedList.size() > idx)
                    linkedList.remove(idx);
            }
        }
        end = System.nanoTime();
        printResult("Удаление по индексу", operations, end - start, decimalPlaces);

        // Получение элемента из начала
        start = System.nanoTime();
        if(list instanceof MyArrayList) {
            MyArrayList<Integer> arrayList = (MyArrayList<Integer>) list;
            for(int i=0; i<operations; i++) {
                if(arrayList.size() > 0)
                    arrayList.get(0);
            }
        } else if(list instanceof MyLinkedList) {
            MyLinkedList<Integer> linkedList = (MyLinkedList<Integer>) list;
            for(int i=0; i<operations; i++) {
                if(linkedList.size() > 0)
                    linkedList.get(0);
            }
        }
        end = System.nanoTime();
        printResult("Получение элемента из начала", operations, end - start, decimalPlaces);

        // Получение элемента из конца
        start = System.nanoTime();
        if(list instanceof MyArrayList) {
            MyArrayList<Integer> arrayList = (MyArrayList<Integer>) list;
            for(int i=0; i<operations; i++) {
                if(arrayList.size() > 0)
                    arrayList.get(arrayList.size() -1);
            }
        } else if(list instanceof MyLinkedList) {
            MyLinkedList<Integer> linkedList = (MyLinkedList<Integer>) list;
            for(int i=0; i<operations; i++) {
                if(linkedList.size() > 0)
                    linkedList.get(linkedList.size() -1);
            }
        }
        end = System.nanoTime();
        printResult("Получение элемента из конца", operations, end - start, decimalPlaces);

        // Получение элемента из середины
        start = System.nanoTime();
        if(list instanceof MyArrayList) {
            MyArrayList<Integer> arrayList = (MyArrayList<Integer>) list;
            for(int i=0; i<operations; i++) {
                int idx = arrayList.size() / 2;
                if(arrayList.size() > idx)
                    arrayList.get(idx);
            }
        } else if(list instanceof MyLinkedList) {
            MyLinkedList<Integer> linkedList = (MyLinkedList<Integer>) list;
            for(int i=0; i<operations; i++) {
                int idx = linkedList.size() / 2;
                if(linkedList.size() > idx)
                    linkedList.get(idx);
            }
        }
        end = System.nanoTime();
        printResult("Получение элемента по индексу", operations, end - start, decimalPlaces);

        // Дополнительный метод: size()
        start = System.nanoTime();
        if(list instanceof MyArrayList) {
            MyArrayList<Integer> arrayList = (MyArrayList<Integer>) list;
            for(int i=0; i<operations; i++) {
                arrayList.size();
            }
        } else if(list instanceof MyLinkedList) {
            MyLinkedList<Integer> linkedList = (MyLinkedList<Integer>) list;
            for(int i=0; i<operations; i++) {
                linkedList.size();
            }
        }
        end = System.nanoTime();
        printResult("size()", operations, end - start, decimalPlaces);
    }

    /**
     * Печатает заголовок таблицы результатов.
     *
     * @param decimalPlaces Количество знаков после запятой для времени.
     */
    protected static void printTableHeader(int decimalPlaces) {
        String separator = "+------------------------------+------------+-------------------+";
        String headerFormat = "| %-28s | %-10s | %-17s |%n";
        System.out.println(separator);
        System.out.printf(headerFormat, "Метод", "Вызовы", "Время (сек.)");
        System.out.println(separator);
    }

    /**
     * Печатает строку результатов тестирования метода.
     *
     * @param method        Название метода.
     * @param operations    Количество вызовов.
     * @param timeNano      Время выполнения в наносекундах.
     * @param decimalPlaces Количество знаков после запятой.
     */
    protected static void printResult(String method, int operations, long timeNano, int decimalPlaces) {
        double timeSeconds = timeNano / 1_000_000_000.0;
        String format = "| %-28s | %-10d | %." + decimalPlaces + "f секунд |%n";
        System.out.printf(format, method, operations, timeSeconds);
    }

    /**
     * Печатает разделитель таблицы.
     */
    private static void printTableSeparator() {
        String separator = "+------------------------------+------------+-------------------+";
        System.out.println(separator);
    }

    /**
     * Получает положительное целое число от пользователя.
     *
     * @param scanner Объект Scanner для чтения ввода.
     * @return Положительное целое число.
     * @throws InputMismatchException Если введено некорректное значение.
     */
    protected static int getPositiveInt(Scanner scanner) throws InputMismatchException {
        int value = scanner.nextInt();
        if(value <= 0)
            throw new InputMismatchException("Число должно быть положительным.");
        return value;
    }

    /**
     * Получает неотрицательное целое число от пользователя.
     *
     * @param scanner Объект Scanner для чтения ввода.
     * @return Неотрицательное целое число.
     * @throws InputMismatchException Если введено некорректное значение.
     */
    protected static int getNonNegativeInt(Scanner scanner) throws InputMismatchException {
        int value = scanner.nextInt();
        if(value < 0)
            throw new InputMismatchException("Число должно быть неотрицательным.");
        return value;
    }
}


//////////////////////////////////////////////////////////////////////////////////////////

PerformanceTestJUnitTests.java///////////////////////////////////////////////////////////////////////////////////////////////// 

  package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.InputMismatchException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса {@link PerformanceTest}, включая тестирование методов работы со списками
 * {@code MyArrayList} и {@code MyLinkedList}, а также проверки корректности пользовательского ввода.
 */
class PerformanceTestTest {
    private MyArrayList<Integer> arrayList;
    private MyLinkedList<Integer> linkedList;

    /**
     * Инициализация перед каждым тестом. Создаются новые экземпляры {@code MyArrayList} и {@code MyLinkedList}.
     */
    @BeforeEach
    public void setUp() {
        arrayList = new MyArrayList<>();
        linkedList = new MyLinkedList<>();
    }

    /**
     * Проверяет, что метод {@code getPositiveInt} корректно принимает положительное целое число.
     */
    @Test
    public void testPositiveInput() {
        String input = "100\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);

        int result = PerformanceTest.getPositiveInt(scanner);
        assertEquals(100, result, "Должно возвращать положительное число");
    }

    /**
     * Проверяет, что метод {@code getPositiveInt} выбрасывает исключение при вводе отрицательного числа.
     */
    @Test
    public void testPositiveInputThrowsException() {
        String input = "-1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);

        assertThrows(InputMismatchException.class, () -> {
            PerformanceTest.getPositiveInt(scanner);
        }, "Ожидается исключение для отрицательного числа");
    }

    /**
     * Проверяет, что метод {@code getNonNegativeInt} корректно принимает неотрицательное целое число.
     */
    @Test
    public void testNonNegativeInput() {
        String input = "0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);

        int result = PerformanceTest.getNonNegativeInt(scanner);
        assertEquals(0, result, "Должно возвращать 0 для неотрицательного числа");
    }

    /**
     * Проверяет, что метод {@code getNonNegativeInt} выбрасывает исключение при вводе отрицательного числа.
     */
    @Test
    public void testNonNegativeInputThrowsException() {
        String input = "-5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);

        assertThrows(InputMismatchException.class, () -> {
            PerformanceTest.getNonNegativeInt(scanner);
        }, "Ожидается исключение для отрицательного числа");
    }

    /**
     * Проверяет добавление и удаление элементов в {@code MyArrayList}.
     */
    @Test
    public void testArrayListAddAndRemove() {
        for (int i = 0; i < 10; i++) {
            arrayList.add(i);
        }
        assertEquals(10, arrayList.size(), "Должно быть добавлено 10 элементов");

        arrayList.remove(0);
        assertEquals(9, arrayList.size(), "После удаления элемента размер должен уменьшиться");
        assertEquals(1, arrayList.get(0), "Первый элемент должен быть смещен после удаления");
    }

    /**
     * Проверяет добавление и удаление элементов в {@code MyLinkedList}.
     */
    @Test
    public void testLinkedListAddAndRemove() {
        for (int i = 0; i < 10; i++) {
            linkedList.add(i);
        }
        assertEquals(10, linkedList.size(), "Должно быть добавлено 10 элементов");

        linkedList.remove(0);
        assertEquals(9, linkedList.size(), "После удаления элемента размер должен уменьшиться");
        assertEquals(1, linkedList.get(0), "Первый элемент должен быть смещен после удаления");
    }

    /**
     * Проверяет получение элемента по индексу в {@code MyArrayList}.
     */
    @Test
    public void testArrayListGet() {
        for (int i = 0; i < 10; i++) {
            arrayList.add(i);
        }
        assertEquals(5, arrayList.get(5), "Должно вернуть элемент по индексу 5");
    }

    /**
     * Проверяет получение элемента по индексу в {@code MyLinkedList}.
     */
    @Test
    public void testLinkedListGet() {
        for (int i = 0; i < 10; i++) {
            linkedList.add(i);
        }
        assertEquals(5, linkedList.get(5), "Должно вернуть элемент по индексу 5");
    }

    /**
     * Проверяет выполнение тестов производительности с пустыми списками.
     */
    @Test
    public void testPerformTestsWithEmptyLists() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        PerformanceTest.performTests(arrayList, 10, 3);
        PerformanceTest.performTests(linkedList, 10, 3);

        String output = outputStream.toString();
        assertTrue(output.contains("Добавление в начало"), "Должен тестироваться метод добавления в начало");
        assertTrue(output.contains("Удаление из конца"), "Должен тестироваться метод удаления из конца");
    }

    /**
     * Проверяет производительность добавления и доступа к элементам в {@code MyArrayList}.
     */
    @Test
    public void testArrayListPerformance() {
        for (int i = 0; i < 1000; i++) {
            arrayList.add(i);
        }
        long start = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            arrayList.get(arrayList.size() / 2);
        }
        long duration = System.nanoTime() - start;

        assertTrue(duration > 0, "Время выполнения должно быть больше 0");
    }

    /**
     * Проверяет производительность добавления и доступа к элементам в {@code MyLinkedList}.
     */
    @Test
    public void testLinkedListPerformance() {
        for (int i = 0; i < 1000; i++) {
            linkedList.add(i);
        }
        long start = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            linkedList.get(linkedList.size() / 2);
        }
        long duration = System.nanoTime() - start;

        assertTrue(duration > 0, "Время выполнения должно быть больше 0");
    }
}


//////////////////////////////////////////////////////////////////////////////////////////
