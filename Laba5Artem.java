pom.xml////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>org.example</groupId>
  <artifactId>Laba5</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>Laba5</name>
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
 * Аннотация для автоматического внедрения зависимостей.
 * Помечает поля, которые должны быть инициализированы с использованием {@link Injector}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface AutoInjectable {}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

SomeInterface.java////////////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;


/**
 * Интерфейс {@code SomeInterface} определяет контракт для выполнения базовой функциональности.
 * <p>
 * Реализация метода {@code doSomething} должна предоставлять основное действие, связанное с интерфейсом.
 */
interface SomeInterface {
    /**
     * Метод для выполнения некоторого демонстрационного действия, реализованного в соответствующем классе.
     */
    void doSomething();
}
  
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

SomeOtherInterface.java////////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

/**
 * Интерфейс {@code SomeOtherInterface} определяет контракт для выполнения дополнительной функциональности.
 * <p>
 * Этот интерфейс используется в системах с автоматической инъекцией зависимостей для демонстрации работы аннотации.
 * Реализация метода {@code doSomeOther} должна предоставлять конкретную логику.
 *
 * @see Injector
 * @see AutoInjectable
 */
interface SomeOtherInterface {
    /**
     * Метод для выполнения некоторой другой функциональности, для демонстрации работы инжектора.
     */
    void doSomeOther();
}
  
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

SomeImpl.java//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

/**
 * Реализация интерфейса {@link SomeInterface}.
 * Выводит "Hello " при вызове метода {@code doSomething()}.
 */
class SomeImpl implements SomeInterface {
    public void doSomething() {
        System.out.println("Hello ");
    }
}
  
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

OtherImpl.java/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

/**
 * Альтернативная реализация интерфейса {@link SomeInterface}.
 * Выводит "Goodbye " при вызове метода {@code doSomething()}.
 */
class OtherImpl implements SomeInterface {
    public void doSomething() {
        System.out.println("Goodbye ");
    }
}
  
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

SODoer.java////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

/**
 * Реализация интерфейса {@link SomeOtherInterface}.
 * Выводит "World!" при вызове метода {@code doSomeOther()}.
 */
class SODoer implements SomeOtherInterface {
    public void doSomeOther() {
        System.out.println("World!");
    }
}
  
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

SomeBean.java//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

/**
 * Класс, содержащий поля, которые будут инициализированы автоматически.
 * Поля помечены аннотацией {@link AutoInjectable}.
 */
class SomeBean {

    /** Поле, которое реализует интерфейс {@link SomeInterface}. */
    @AutoInjectable
    private SomeInterface field1;

    /** Поле, которое реализует интерфейс {@link SomeOtherInterface}. */
    @AutoInjectable
    private SomeOtherInterface field2;

    /**
     * Метод, вызывающий действия на инжектированных полях.
     * @throws RuntimeException если поля не были инициализированы.
     */
    public void foo() {
        if (field1 == null || field2 == null) {
            throw new RuntimeException("Fields are not initialized.");
        }
        field1.doSomething();
        field2.doSomeOther();
    }
}
  
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

Injector.java//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Класс для автоматического внедрения зависимостей в объекты.
 * Загружает реализации из файла `config.properties`.
 */
class Injector {
    /** Свойства, содержащие сопоставления интерфейсов и их реализаций. */
    private Properties properties;

    /**
     * Создает новый объект {@link Injector}, загружая зависимости из указанного файла.
     *
     * @param propertiesPath путь к файлу `config.properties`.
     * @throws IOException если файл не может быть прочитан.
     */
    public Injector(String propertiesPath) throws IOException {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(propertiesPath)) {
            properties.load(fis);
        }
    }

    /**
     * Внедряет зависимости в объект, используя аннотацию {@link AutoInjectable}.
     *
     * @param obj объект, в который нужно внедрить зависимости.
     * @param <T> тип объекта.
     * @return объект с инжектированными зависимостями.
     * @throws RuntimeException если зависимость не может быть инжектирована.
     */
    public <T> T inject(T obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(AutoInjectable.class)) {
                Class<?> fieldType = field.getType();
                String implClassName = properties.getProperty(fieldType.getName());

                if (implClassName != null) {
                    try {
                        System.out.println("Injecting implementation: " + implClassName + " for field: " + field.getName());
                        Class<?> implClass = Class.forName(implClassName);
                        Object implInstance = implClass.getDeclaredConstructor().newInstance();

                        field.setAccessible(true);
                        field.set(obj, implInstance);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to inject dependency for field: " + field.getName(), e);
                    }
                } else {
                    System.out.println("No implementation found for: " + fieldType.getName());
                }
            }
        }
        return obj;
    }
}
  
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

Main.java//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

import java.io.IOException;

/**
 * Главный класс программы. Проверяет работу инжектора зависимостей.
 *
 * <p>Программа выполняет следующие шаги:</p>
 * <ol>
 *     <li>Загружает конфигурационный файл `config.properties`, содержащий сопоставления интерфейсов и их реализаций.</li>
 *     <li>Создает объект {@link Injector}, который будет заниматься инжекцией зависимостей.</li>
 *     <li>Создает экземпляр {@link SomeBean} и автоматически инициализирует его поля с использованием аннотации {@link AutoInjectable}.</li>
 *     <li>Вызывает метод {@link SomeBean#foo()}, который демонстрирует работу инжектированных зависимостей.</li>
 * </ol>
 *
 * <p>При возникновении ошибок (например, отсутствия файла конфигурации или неверного пути) программа выводит соответствующее сообщение в консоль.</p>
 */
public class Main {
    public static void main(String[] args) {
        try {
            // Загрузка файла из classpath
            String propertiesPath = Main.class.getClassLoader().getResource("config.properties").getPath();
            Injector injector = new Injector(propertiesPath);

            SomeBean someBean = injector.inject(new SomeBean());
            someBean.foo();
        } catch (IOException e) {
            System.err.println("Failed to load properties: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Runtime error: " + e.getMessage());
        }
    }
}

  
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

resources/config.properties////////////////////////////////////////////////////////////////////////////////////////////////////

org.example.SomeInterface=org.example.SomeImpl
org.example.SomeOtherInterface=org.example.SODoer

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

InjectorTest.java//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Тестовый класс для проверки функциональности {@link Injector}.
 * <p>
 * Включает тесты для проверки корректности инъекции зависимостей, обработки ошибок и обработки различных сценариев.
 */
public class InjectorTest {

    /**
     * Тест проверяет успешную инъекцию зависимостей в объект {@link SomeBean}.
     * <p>
     * Создается временный файл `config.properties`, в котором указываются корректные сопоставления интерфейсов и их реализаций.
     * Затем с помощью {@link Injector} выполняется инъекция зависимостей, и вызывается метод {@link SomeBean#foo()}.
     * <p>
     * Успешное выполнение метода {@code foo()} без выбрасывания исключений подтверждает корректность работы.
     *
     * @throws IOException если возникают ошибки при создании временного файла.
     */
    @Test
    public void testSuccessfulInjection() throws IOException {
        // Создаем временный config.properties
        File tempConfig = File.createTempFile("config", ".properties");
        try (FileWriter writer = new FileWriter(tempConfig)) {
            writer.write("org.example.SomeInterface=org.example.SomeImpl\n");
            writer.write("org.example.SomeOtherInterface=org.example.SODoer\n");
        }

        // Создаем инжектор
        Injector injector = new Injector(tempConfig.getAbsolutePath());
        SomeBean someBean = injector.inject(new SomeBean());

        // Проверяем, что поля были инициализированы
        assertDoesNotThrow(() -> someBean.foo());
    }

    /**
     * Тест проверяет поведение {@link Injector}, если в файле конфигурации отсутствует реализация для одного из интерфейсов.
     * <p>
     * Создается временный файл `config.properties`, содержащий запись только для одного интерфейса.
     * После инъекции вызов метода {@link SomeBean#foo()} должен выбросить {@link RuntimeException},
     * так как одно из полей объекта остается неинициализированным.
     *
     * @throws IOException если возникают ошибки при создании временного файла.
     */
    @Test
    public void testMissingImplementation() throws IOException {
        // Создаем временный config.properties с отсутствующей записью
        File tempConfig = File.createTempFile("config", ".properties");
        try (FileWriter writer = new FileWriter(tempConfig)) {
            writer.write("org.example.SomeOtherInterface=org.example.SODoer\n");
        }

        // Создаем инжектор
        Injector injector = new Injector(tempConfig.getAbsolutePath());
        SomeBean someBean = injector.inject(new SomeBean());

        // Проверяем, что RuntimeException выброшено из-за отсутствия field1
        RuntimeException exception = assertThrows(RuntimeException.class, someBean::foo);
        assertTrue(exception.getMessage().contains("Fields are not initialized"));
    }

    /**
     * Тест проверяет поведение {@link Injector}, если указан некорректный путь к файлу конфигурации.
     * <p>
     * Ожидается, что при попытке создать {@link Injector} с несуществующим файлом будет выброшено {@link IOException}.
     */
    @Test
    public void testInvalidConfigPath() {
        IOException exception = assertThrows(IOException.class, () -> new Injector("invalid_path.properties"));
        assertTrue(exception.getMessage().contains("invalid_path.properties"));
    }

    /**
     * Тест проверяет поведение {@link Injector}, если в файле конфигурации указано некорректное имя класса реализации.
     * <p>
     * Создается временный файл `config.properties`, в котором указан несуществующий класс для одного из интерфейсов.
     * Ожидается, что попытка инъекции зависимостей вызовет {@link RuntimeException}.
     *
     * @throws IOException если возникают ошибки при создании временного файла.
     */
    @Test
    public void testInvalidClassInConfig() throws IOException {
        // Создаем временный config.properties с некорректным классом
        File tempConfig = File.createTempFile("config", ".properties");
        try (FileWriter writer = new FileWriter(tempConfig)) {
            writer.write("org.example.SomeInterface=org.example.NonExistentClass\n");
        }

        // Создаем инжектор
        Injector injector = new Injector(tempConfig.getAbsolutePath());
        SomeBean someBean = new SomeBean();

        // Проверяем, что выбрасывается исключение
        RuntimeException exception = assertThrows(RuntimeException.class, () -> injector.inject(someBean));
        assertTrue(exception.getMessage().contains("Failed to inject dependency"));
    }
}


  
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




