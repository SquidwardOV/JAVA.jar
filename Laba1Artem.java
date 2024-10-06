pom.xml

  <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>org.example</groupId>
  <artifactId>Laba1Artem</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>Laba1Artem</name>
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
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.7.0</version>
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

  ////////////////////////////////////////////////////////////////////////////////////////////////////////
Container.java

package org.example;

import java.util.Scanner;

public class Container<T> {
    private Object[] elements;
    private int size; // Текущий размер массива
    private int capacity; // Вместимость массива

    // Конструктор с начальной вместимостью
    public Container() {
        capacity = 2; // Начальная вместимость (можно выбрать любую)
        elements = new Object[capacity];
        size = 0;
    }

    // Метод для добавления элемента
    public void add(T element) {
        if (size == capacity) {
            resize(capacity * 2); // Увеличиваем вместимость в 2 раза при необходимости
        }
        elements[size++] = element;
    }

    // Метод для извлечения элемента по индексу
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс выходит за пределы массива");
        }
        return (T) elements[index];
    }

    // Метод для удаления элемента по индексу
    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс выходит за пределы массива");
        }
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null; // Уменьшаем размер и обнуляем последнюю ссылку

        // Уменьшаем размер массива, если он становится слишком большим
        if (size > 0 && size == capacity / 4) {
            resize(capacity / 2);
        }
    }

    // Метод для изменения размера массива
    private void resize(int newCapacity) {
        capacity = newCapacity;
        Object[] newElements = new Object[capacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
    }

    // Метод для получения текущего размера контейнера
    public int size() {
        return size;
    }

    // Метод для проверки, пуст ли контейнер
    public boolean isEmpty() {
        return size == 0;
    }

    // Метод для очистки контейнера
    public void clear() {
        elements = new Object[capacity];
        size = 0;
    }

    // Метод для отображения всех элементов контейнера
    public void showAllElements() {
        if (isEmpty()) {
            System.out.println("Контейнер пуст.");
        } else {
            System.out.println("Элементы контейнера:");
            for (int i = 0; i < size; i++) {
                System.out.println("[" + i + "] " + elements[i]);
            }
        }
    }

    // Интерфейс для взаимодействия с пользователем
    public static void main(String[] args) {
        Container<String> container = new Container<>();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nВыберите действие:");
            System.out.println("1 - Добавить элемент");
            System.out.println("2 - Удалить элемент по индексу");
            System.out.println("3 - Получить элемент по индексу");
            System.out.println("4 - Показать размер контейнера");
            System.out.println("5 - Очистить контейнер");
            System.out.println("6 - Показать все элементы");
            System.out.println("7 - Выход");
            System.out.print("Ваш выбор: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Считываем символ новой строки после выбора

            switch (choice) {
                case 1:
                    System.out.print("Введите элемент для добавления: ");
                    String element = scanner.nextLine();
                    container.add(element);
                    System.out.println("Элемент добавлен.");
                    break;

                case 2:
                    if (container.isEmpty()) {
                        System.out.println("Контейнер пуст.");
                    } else {
                        System.out.print("Введите индекс для удаления: ");
                        int indexToRemove = scanner.nextInt();
                        try {
                            container.remove(indexToRemove);
                            System.out.println("Элемент удален.");
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("Ошибка: " + e.getMessage());
                        }
                    }
                    break;

                case 3:
                    if (container.isEmpty()) {
                        System.out.println("Контейнер пуст.");
                    } else {
                        System.out.print("Введите индекс для получения элемента: ");
                        int indexToGet = scanner.nextInt();
                        try {
                            String retrievedElement = container.get(indexToGet);
                            System.out.println("Элемент по индексу " + indexToGet + ": " + retrievedElement);
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("Ошибка: " + e.getMessage());
                        }
                    }
                    break;

                case 4:
                    System.out.println("Текущий размер контейнера: " + container.size());
                    break;

                case 5:
                    container.clear();
                    System.out.println("Контейнер очищен.");
                    break;

                case 6:
                    container.showAllElements();
                    break;

                case 7:
                    System.out.println("Выход из программы.");
                    running = false;
                    break;

                default:
                    System.out.println("Неверный выбор, попробуйте снова.");
                    break;
            }
        }

        scanner.close();
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////

ContainerTest.java

  package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ContainerTest {

    private Container<String> container;

    @BeforeEach
    void setUp() {
        container = new Container<>();
    }

    @Test
    void testAddElement() {
        container.add("Element1");
        container.add("Element2");

        assertEquals(2, container.size(), "Размер контейнера должен быть 2");
        assertEquals("Element1", container.get(0), "Первый элемент должен быть 'Element1'");
        assertEquals("Element2", container.get(1), "Второй элемент должен быть 'Element2'");
    }

    @Test
    void testGetElementOutOfBounds() {
        container.add("Element1");
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            container.get(5);
        });

        assertEquals("Индекс выходит за пределы массива", exception.getMessage());
    }

    @Test
    void testRemoveElement() {
        container.add("Element1");
        container.add("Element2");
        container.remove(0);

        assertEquals(1, container.size(), "Размер контейнера должен быть 1 после удаления");
        assertEquals("Element2", container.get(0), "Теперь первый элемент должен быть 'Element2'");
    }

    @Test
    void testRemoveElementOutOfBounds() {
        container.add("Element1");
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            container.remove(5);
        });

        assertEquals("Индекс выходит за пределы массива", exception.getMessage());
    }

    @Test
    void testClearContainer() {
        container.add("Element1");
        container.add("Element2");
        container.clear();

        assertEquals(0, container.size(), "Размер контейнера должен быть 0 после очистки");
        assertTrue(container.isEmpty(), "Контейнер должен быть пуст после очистки");
    }

    @Test
    void testIsEmpty() {
        assertTrue(container.isEmpty(), "Контейнер должен быть пуст после создания");
        container.add("Element1");
        assertFalse(container.isEmpty(), "Контейнер не должен быть пуст после добавления элемента");
    }

    @Test
    void testResizeContainer() {
        container.add("Element1");
        container.add("Element2");
        container.add("Element3");

        assertEquals(3, container.size(), "Размер контейнера должен быть 3 после добавления трех элементов");
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
