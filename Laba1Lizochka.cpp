pom.xml
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////
  <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>org.example</groupId>
  <artifactId>untitled1</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>untitled1</name>
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
      <artifactId>junit-jupiter-api</artifactId>
      <version>5.7.0</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-engine</artifactId>
      <version>5.7.0</version>
    </dependency>
  </dependencies>
</project>
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
App.java

package org.example;
import java.util.Scanner;

class Node {
    Object data;
    Node next;

    Node(Object data) {
        this.data = data;
        this.next = null;
    }
}

class CustomContainer {
    private Node head;
    private int size;

    public CustomContainer() {
        this.head = null;
        this.size = 0;
    }

    // Метод для добавления элемента
    public void add(Object item) {
        Node newNode = new Node(item);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    // Метод для извлечения элемента по индексу
    public Object get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    // Метод для удаления элемента по индексу
    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        if (index == 0) {
            head = head.next;
        } else {
            Node current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            current.next = current.next.next;
        }
        size--;
    }

    // Метод для получения размера контейнера
    public int size() {
        return size;
    }

    // Метод для проверки, пуст ли контейнер
    public boolean isEmpty() {
        return size == 0;
    }

    // Метод для отображения всех элементов контейнера
    public void display() {
        Node current = head;
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        CustomContainer container = new CustomContainer();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nВыберите действие:");
            System.out.println("1. Добавить элемент");
            System.out.println("2. Удалить элемент по индексу");
            System.out.println("3. Получить элемент по индексу");
            System.out.println("4. Показать все элементы");
            System.out.println("5. Показать размер контейнера");
            System.out.println("0. Выход");
            choice = scanner.nextInt();
            scanner.nextLine(); // Чистим буфер после ввода числа

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Введите элемент для добавления: ");
                        String item = scanner.nextLine();
                        container.add(item);
                        break;

                    case 2:
                        if (container.isEmpty()) {
                            System.out.println("Контейнер пуст. Невозможно удалить элемент.");
                        } else {
                            System.out.print("Введите индекс элемента для удаления: ");
                            int removeIndex = scanner.nextInt();
                            container.remove(removeIndex);
                            System.out.println("Элемент с индексом " + removeIndex + " был удалён.");
                        }
                        break;

                    case 3:
                        if (container.isEmpty()) {
                            System.out.println("Контейнер пуст. Невозможно получить элемент.");
                        } else {
                            int getIndex = scanner.nextInt();
                            System.out.println("Элемент: " + container.get(getIndex));
                        }
                        break;

                    case 4:
                        System.out.println("Элементы контейнера:");
                        container.display();
                        break;

                    case 5:
                        System.out.println("Размер контейнера: " + container.size());
                        break;

                    case 0:
                        System.out.println("Выход из программы.");
                        break;

                    default:
                        System.out.println("Неверный выбор, попробуйте снова.");
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Ошибка: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Произошла ошибка: " + e.getMessage());
            }
        } while (choice != 0);
        scanner.close();
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
Tests.java

package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomContainerTest {

    private CustomContainer container;

    @BeforeEach
    void setUp() {
        container = new CustomContainer();
    }

    @Test
    void testAddAndGet() {
        container.add("Element 1");
        container.add("Element 2");
        assertEquals("Element 1", container.get(0));
        assertEquals("Element 2", container.get(1));
    }

    @Test
    void testRemove() {
        container.add("Element 1");
        container.add("Element 2");
        container.remove(0);
        assertEquals("Element 2", container.get(0));
        assertEquals(1, container.size());
    }

    @Test
    void testSize() {
        assertEquals(0, container.size());
        container.add("Element 1");
        assertEquals(1, container.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(container.isEmpty());
        container.add("Element 1");
        assertFalse(container.isEmpty());
    }

    @Test
    void testGetInvalidIndex() {
        container.add("Element 1");
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            container.get(1);
        });
        assertEquals("Invalid index: 1", exception.getMessage());
    }

    @Test
    void testRemoveInvalidIndex() {
        container.add("Element 1");
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            container.remove(1);
        });
        assertEquals("Invalid index: 1", exception.getMessage());
    }

    @Test
    void testDisplay() {
        container.add("Element 1");
        container.add("Element 2");
        // Это пример теста, вывод на консоль лучше тестировать вручную или с помощью специальных библиотек для перехвата консольного вывода
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
