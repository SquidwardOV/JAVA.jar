pom.xml////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>org.example</groupId>
  <artifactId>Laba5.1</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>Laba5.1</name>
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

  
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

Annotation.java////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

import java.lang.annotation.*;

/**
 * Аннотация для пометки полей, которые требуют автоматической инъекции зависимостей.
 * <p>
 * Используется в сочетании с классом {@link Injector}, который выполняет автоматическую инъекцию объектов в такие поля.
 * </p>
 * <p>
 * Аннотация применима только к полям ({@link ElementType#FIELD}) и доступна во время выполнения программы ({@link RetentionPolicy#RUNTIME}).
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface AutoInjectable {
}
  
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

FirstInterface.java////////////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

/**
 * Интерфейс для демонстрации инъекции зависимости.
 * <p>
 * Этот интерфейс определяет метод {@link #performAction()}, который
 * должен быть реализован для выполнения определённого действия.
 * Инъекция зависимостей позволяет динамически выбирать реализацию
 * интерфейса во время выполнения программы.
 * </p>
 */
public interface FirstInterface {
    /**
     * Выполняет некоторое действие.
     * <p>
     * Реализации этого метода, такие как {@link FirstImplementation} или
     * {@link AlternateFirstImplementation}, выводят сообщения в консоль
     * для демонстрации своей работы.
     * </p>
     */
    void performAction();
}
  
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

SecondInterface.java////////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

/**
 * Ещё один интерфейс для демонстрации инъекции зависимости.
 * <p>
 * Этот интерфейс определяет метод {@link #executeTask()}, который должен
 * быть реализован для выполнения конкретной задачи. Инъекция зависимостей
 * позволяет динамически выбирать реализацию интерфейса во время выполнения
 * программы.
 * </p>
 */
public interface SecondInterface {
    /**
     * Выполняет задачу.
     * <p>
     * Реализации этого метода, такие как {@link SecondImplementation}, выводят
     * сообщения в консоль для демонстрации своей работы.
     * </p>
     */
    void executeTask();
}
  
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

FirstImplementation.java//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

/**
 * Реализация интерфейса {@link FirstInterface}.
 * <p>
 * Этот класс реализует метод {@link FirstInterface#performAction()},
 * который выводит сообщение в консоль. Используется для демонстрации
 * стандартной реализации интерфейса.
 * </p>
 */
public class FirstImplementation implements FirstInterface {
    /**
     * Выполняет вывод указанного сообщения в консоль.
     */
    @Override
    public void performAction() {
        System.out.println("С наступающим ");
    }
}
  
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

AlternateFirstImplementation.java/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

/**
 * Альтернативная реализация интерфейса {@link FirstInterface}.
 * <p>
 * Этот класс реализует метод {@link FirstInterface#performAction()},
 * который выводит альтернативное сообщение в консоль. Используется
 * для демонстрации возможности переключения между реализациями
 * интерфейса.
 * </p>
 */
public class AlternateFirstImplementation implements FirstInterface {
    /**
     * Выполняет альтернативное действие, выводя сообщение в консоль.
     */
    @Override
    public void performAction() {
        System.out.println("С прошедшим");
    }
}
  
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

SecondImplementation.java////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

/**
 * Реализация интерфейса {@link SecondInterface}.
 * <p>
 * Этот класс реализует метод {@link SecondInterface#executeTask()},
 * который выводит сообщение в консоль. Используется для демонстрации
 * инъекции зависимостей.
 * </p>
 */
public class SecondImplementation implements SecondInterface {
    /**
     * Выполняет вывод указанного сообщения в консоль.
     */
    @Override
    public void executeTask() {
        System.out.println("Новым годом!");
    }
}
  
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

TargetClass.java//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

/**
 * Класс, для которого будет осуществлена инъекция зависимостей.
 */
public class TargetClass {

    /**
     * Поле, требующее инъекции зависимости, реализующей FirstInterface.
     */
    @AutoInjectable
    private FirstInterface firstDependency;

    /**
     * Поле, требующее инъекции зависимости, реализующей SecondInterface.
     */
    @AutoInjectable
    private SecondInterface secondDependency;

    /**
     * Вызывает методы зависимостей для демонстрации их работы.
     *
     * Если инъекция прошла успешно, на консоль будут выведены сообщения,
     * определённые в методах implement-ов.
     */
    public void display() {
        firstDependency.performAction();
        secondDependency.executeTask();
    }
}
  
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

Injector.java//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import java.lang.reflect.Field;


/**
 * Класс, выполняющий инъекцию зависимостей в объекты.
 */
public class Injector {

    /**
     * Хранит свойства для определения реализации интерфейсов.
     */
    private Properties properties;

    /**
     * Конструктор, загружающий зависимости из файла properties в classpath.
     *
     * Файл должен содержать строки вида:
     * <pre>
     * InterfaceName=ImplementationClassName
     * </pre>
     * @throws RuntimeException если файл не найден или не удаётся загрузить свойства.
     */
    public Injector() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("dependencies.properties")) {
            if (input == null) {
                throw new RuntimeException("Файл properties не найден в classpath.");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить файл свойств.", e);
        }
    }

    /**
     * Конструктор, принимающий готовые свойства для инъекции (например, для тестов).
     *
     * @param properties свойства с маппингом интерфейсов и их реализаций.
     */
    public Injector(Properties properties) {
        this.properties = properties;
    }

    /**
     * Инъецирует зависимости в объект, аннотированные @AutoInjectable.
     *
     * @param targetObject объект, в который требуется внедрить зависимости.
     * @param <T> тип объекта.
     * @return объект с внедрёнными зависимостями.
     * @throws RuntimeException если инъекция не может быть выполнена.
     */
    public <T> T inject(T targetObject) {
        Class<?> targetClass = targetObject.getClass();

        for (Field field : targetClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(AutoInjectable.class)) {
                String interfaceName = field.getType().getName();
                String implementationName = properties.getProperty(interfaceName);

                if (implementationName == null) {
                    throw new RuntimeException("Не найдена реализация для " + interfaceName);
                }

                try {
                    Class<?> implementationClass = Class.forName(implementationName);
                    Object implementationInstance = implementationClass.getDeclaredConstructor().newInstance();
                    field.setAccessible(true);
                    field.set(targetObject, implementationInstance);
                    System.out.println("Для поля " + field.getName() + " успешно инъектирован класс " + implementationName);
                } catch (Exception e) {
                    throw new RuntimeException("Не удалось выполнить инъекцию для поля " + field.getName(), e);
                }
            }
        }

        return targetObject;
    }
}


  
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

Main.java//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

/**
 * Главный класс программы, демонстрирующий работу механизма инъекции зависимостей.
 * <p>
 * Программа реализует автоматическую инъекцию зависимостей в поля класса {@link TargetClass}
 * с использованием механизма рефлексии. После успешной инъекции демонстрируется вызов методов
 * внедрённых зависимостей.
 * </p>
 */
public class Main {

    /**
     * Точка входа в программу.
     * <p>
     * В данном методе выполняется следующее:
     * <ul>
     *     <li>Создаётся объект {@link Injector} для управления процессом инъекции зависимостей.</li>
     *     <li>Инициализируется объект {@link TargetClass}, который содержит поля для инъекции.</li>
     *     <li>Вызывается метод {@link Injector#inject(Object)}, который автоматически внедряет зависимости в объект {@link TargetClass}.</li>
     *     <li>Демонстрируется работа внедрённых зависимостей путём вызова метода {@link TargetClass#display()}.</li>
     * </ul>
     * </p>
     * <p>
     * Программа ожидает, что файл конфигурации (например, {@code dependencies.properties})
     * находится в classpath и содержит информацию о том, какие реализации должны быть инъектированы
     * для интерфейсов, используемых в {@link TargetClass}.
     * </p>
     *
     * @param args аргументы командной строки (в данной программе не используются).
     */
    public static void main(String[] args) {
        // Создаём инжектор для выполнения инъекции зависимостей
        Injector injector = new Injector();

        // Создаём экземпляр класса TargetClass
        TargetClass target = new TargetClass();

        // Выполняем инъекцию зависимостей
        target = injector.inject(target);

        // Демонстрируем работу методов внедрённых зависимостей
        target.display();
    }
}

  
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

resources/dependencies.properties////////////////////////////////////////////////////////////////////////////////////////////////////

org.example.FirstInterface=org.example.FirstImplementation
org.example.SecondInterface=org.example.SecondImplementation
  
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

InjectorTest.java//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;

/**
 * Тестовый класс для проверки функциональности класса {@link Injector}.
 *
 * Проверяет корректность инъекции зависимостей, обработки ошибок и работы с несколькими полями.
 */
public class InjectorTest {

    /**
     * Проверяет корректность работы метода {@link Injector#inject(Object)}.
     *
     * <p>Тест создает объект {@link TargetClass}, инъектирует в него зависимости
     * и проверяет, что вывод метода {@code display()} соответствует ожидаемому.</p>
     *
     * <p>Вывод о заинъектированных полях отфильтровывается, чтобы сравнивать только строки,
     * относящиеся к результату работы метода {@code display()}.</p>
     */
    @Test
    public void testInjection() {
        // Создаём инжектор и объект для тестирования
        Injector injector = new Injector();
        TargetClass target = injector.inject(new TargetClass());

        // Перенаправляем стандартный вывод для проверки
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Вызываем метод display(), который должен вывести две строки
        target.display();

        // Ожидаемый вывод только от метода display()
        String expectedOutput = "С наступающим \n" +
                "Новым годом!";

        // Возвращаем стандартный вывод
        System.setOut(originalOut);

        // Разбиваем весь вывод на строки
        String[] lines = outContent.toString().split("\\r?\\n");
        StringBuilder filteredOutput = new StringBuilder();

        // Перебираем каждую строку и выбираем только нужные
        for (String line : lines) {
            if (line.startsWith("С наступающим ") || line.startsWith("Новым годом!")) {
                if (filteredOutput.length() > 0) {
                    filteredOutput.append("\n");
                }
                filteredOutput.append(line);
            }
        }

        // Сравниваем только отфильтрованные строки с ожидаемым результатом
        assertEquals(expectedOutput, filteredOutput.toString().trim());
    }

    /**
     * Проверяет, что метод {@link Injector#inject(Object)} выбрасывает исключение,
     * если для поля не задана реализация в конфигурации.
     *
     * <p>Тест использует класс с полем, которое должно быть инъектировано, но
     * в переданных свойствах отсутствует реализация для этого поля. Ожидается,
     * что будет выброшено исключение {@link RuntimeException} с соответствующим сообщением.</p>
     */
    @Test
    public void testMissingImplementation() {
        // Проверка, что исключение выбрасывается при отсутствии реализации
        Properties properties = new Properties();
        Injector injector = new Injector(properties);

        class InvalidClass {
            @AutoInjectable
            private FirstInterface missingDependency;
        }

        // Ожидаем исключение RuntimeException
        Exception exception = assertThrows(RuntimeException.class, () -> {
            injector.inject(new InvalidClass());
        });

        // Проверяем текст исключения
        assertTrue(exception.getMessage().contains("Не найдена реализация для"));
    }

    /**
     * Проверяет, что метод {@link Injector#inject(Object)} корректно инъектирует
     * несколько зависимостей в один объект.
     *
     * <p>Тест создает объект {@link TargetClass} с несколькими полями, которые
     * должны быть инъектированы. Проверяется, что все поля успешно инъектированы
     * без выброса исключений.</p>
     */
    @Test
    public void testMultipleFieldsInjection() {
        // Проверка, что несколько полей успешно инъектируются
        Injector injector = new Injector();
        TargetClass target = injector.inject(new TargetClass());

        // Убедимся, что объект не null
        assertNotNull(target);

        // Выполняем метод display() и проверяем, что исключения не выбрасываются
        assertDoesNotThrow(target::display);
    }

    /**
     * Проверяет, что метод {@link Injector#inject(Object)} выбрасывает исключение,
     * если указанный в конфигурации класс реализации не существует.
     *
     * <p>Тест создает свойства, в которых для одного из полей задан несуществующий
     * класс. Ожидается, что будет выброшено исключение {@link RuntimeException} с
     * сообщением, указывающим на проблему.</p>
     */
    @Test
    public void testInvalidImplementationClass() {
        // Создаём свойства с неверным классом реализации
        Properties properties = new Properties();
        properties.setProperty("FirstInterface", "NonExistentClass");
        Injector injector = new Injector(properties);

        class InvalidImplementation {
            @AutoInjectable
            private FirstInterface invalidDependency;
        }

        // Ожидаем исключение RuntimeException
        Exception exception = assertThrows(RuntimeException.class, () -> {
            injector.inject(new InvalidImplementation());
        });

        // Проверяем текст исключения
        assertTrue(exception.getMessage().contains("Не найдена реализация для"));
    }
}

  
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




