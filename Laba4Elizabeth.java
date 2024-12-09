//В директорию src была добавлена папка resources, в которую был помещён исходный файл foreign_names.csv, библиотеки opencsv и commons-lang3-3.12.0 подключены проекту, как .jar файлы
pom.xml///////////////////////////////////////////////////////////////////////////////////////////////////////

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>org.example</groupId>
  <artifactId>Laba4.2</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>Laba4.2</name>
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
      <artifactId>junit-jupiter</artifactId>
      <version>RELEASE</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
</project>


  
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

Employee.java/////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

import java.util.Date;

/**
 * Класс, представляющий сотрудника.
 */
public class Employee {
    private int employeeId; // Уникальный идентификатор сотрудника
    private String fullName; // Полное имя сотрудника
    private String genderIdentity; // Пол сотрудника
    private Department workDepartment; // Департамент сотрудника
    private double monthlyIncome; // Зарплата сотрудника
    private Date birthdate; // Дата рождения сотрудника

    /**
     * Конструктор для создания объекта сотрудника.
     *
     * @param id          Уникальный идентификатор сотрудника
     * @param name        Полное имя сотрудника
     * @param gender      Пол сотрудника
     * @param department  Департамент, к которому относится сотрудник
     * @param salary      Зарплата сотрудника
     * @param birthdate   Дата рождения сотрудника
     */
    public Employee(int id, String name, String gender, Department department, double salary, Date birthdate) {
        this.employeeId = id;
        this.fullName = name;
        this.genderIdentity = gender;
        this.workDepartment = department;
        this.monthlyIncome = salary;
        this.birthdate = birthdate;
    }

    /**
     * Получить ID сотрудника.
     *
     * @return ID сотрудника
     */
    public int getEmployeeId() {
        return employeeId;
    }

    /**
     * Получить полное имя сотрудника.
     *
     * @return Полное имя сотрудника
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Получить пол сотрудника.
     *
     * @return Пол сотрудника
     */
    public String getGenderIdentity() {
        return genderIdentity;
    }

    /**
     * Получить департамент сотрудника.
     *
     * @return Департамент сотрудника
     */
    public Department getWorkDepartment() {
        return workDepartment;
    }

    /**
     * Получить зарплату сотрудника.
     *
     * @return Зарплата сотрудника
     */
    public double getMonthlyIncome() {
        return monthlyIncome;
    }

    /**
     * Получить дату рождения сотрудника.
     *
     * @return Дата рождения сотрудника
     */
    public Date getBirthdate() {
        return birthdate;
    }
}



//////////////////////////////////////////////////////////////////////////////////////////////////////////////

Department.java///////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

/**
 * Класс, представляющий департамент, к которому относится сотрудник.
 */
public class Department {
    private static int uniqueId = 1; // Уникальный идентификатор для департамента
    private int departmentId;
    private String departmentName;

    /**
     * Конструктор для создания объекта департамента.
     *
     * @param name Название департамента
     */
    public Department(String name) {
        this.departmentId = uniqueId++;
        this.departmentName = name;
    }

    /**
     * Получить ID департамента.
     *
     * @return ID департамента
     */
    public int getDepartmentId() {
        return departmentId;
    }

    /**
     * Получить название департамента.
     *
     * @return Название департамента
     */
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * Переопределение метода equals для корректного сравнения объектов департамента.
     *
     * @param obj Объект для сравнения
     * @return true, если объекты равны, иначе false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Department that = (Department) obj;

        return departmentName.equals(that.departmentName);
    }

    /**
     * Переопределение метода hashCode для использования в коллекциях.
     *
     * @return Хэш-код объекта
     */
    @Override
    public int hashCode() {
        return departmentName.hashCode();
    }
}


/////////////////////////////////////////////////////////////////////////////////////////////////////////////

Main.java///////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVParserBuilder;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Главный класс программы, который читает данные из CSV-файла,
 * создает список сотрудников и выводит его в файл.
 */
public class Main {

    /**
     * Точка входа в программу.
     *
     * <p>
     * Программа выполняет следующие шаги:
     * <ul>
     *     <li>Создает пустую директорию департаментов.</li>
     *     <li>Читает данные сотрудников из CSV-файла.</li>
     *     <li>Создает список сотрудников и добавляет их в соответствующие департаменты.</li>
     *     <li>Записывает список сотрудников в файл.</li>
     * </ul>
     * </p>
     *
     * @param args Аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        String filePath = "foreign_names.csv"; // Имя CSV-файла в ресурсах
        char delimiter = ';'; // Разделитель в CSV-файле
        String outputFilePath = "employees_output.txt"; // Имя выходного файла

        // Создание пустой директории департаментов
        Map<String, Department> deptDirectory = createDeptDirectory();

        try {
            // Открытие потока для чтения файла
            InputStream fileStream = Main.class.getClassLoader().getResourceAsStream(filePath);

            // Если файл не найден, выбрасывается исключение
            if (fileStream == null) {
                throw new FileNotFoundException("Не удалось найти файл: " + filePath);
            }

            // Чтение данных из CSV-файла и создание списка сотрудников
            List<Employee> staffList = readCsvAndCreateEmployeeList(fileStream, delimiter, deptDirectory);

            // Запись списка сотрудников в файл
            writeEmployeesToFile(staffList, outputFilePath);
            System.out.printf("List с сотрудниками успешно сформирован и для его отладки записан в файл: %s%n", outputFilePath);

        } catch (Exception ex) {
            // Обработка исключений
            System.err.println("Произошла ошибка при обработке файла: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Создает и возвращает пустую директорию департаментов.
     *
     * <p>
     * Используется для хранения департаментов с уникальными названиями.
     * Если департамент уже существует, он не создается заново.
     * </p>
     *
     * @return Пустая {@link Map} для хранения департаментов.
     */
    public static Map<String, Department> createDeptDirectory() {
        return new HashMap<>();
    }

    /**
     * Читает данные из CSV-файла и создает список сотрудников.
     *
     * <p>
     * Для каждой строки в CSV-файле:
     * <ul>
     *     <li>Считываются ID, имя, пол, дата рождения, департамент и зарплата.</li>
     *     <li>Создается объект {@link Employee}.</li>
     *     <li>Если департамент отсутствует в директории, он создается и добавляется в неё.</li>
     *     <li>Сотрудник добавляется в общий список сотрудников.</li>
     * </ul>
     * </p>
     *
     * @param fileStream    Поток для чтения файла.
     * @param delimiter     Разделитель в CSV-файле.
     * @param deptDirectory Словарь для хранения департаментов.
     * @return Список сотрудников, созданный на основе данных из CSV-файла.
     * @throws Exception Если произошла ошибка при чтении файла или парсинге данных.
     */
    public static List<Employee> readCsvAndCreateEmployeeList(InputStream fileStream, char delimiter, Map<String, Department> deptDirectory) throws Exception {
        List<Employee> staffList = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

        // Создание CSVReader для чтения файла
        CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(fileStream))
                .withCSVParser(new CSVParserBuilder().withSeparator(delimiter).build())
                .build();

        String[] record;
        boolean skipHeader = true; // Флаг для пропуска первой строки (заголовок)

        // Чтение строк из CSV-файла
        while ((record = csvReader.readNext()) != null) {
            if (skipHeader) {
                skipHeader = false; // Пропускаем заголовок
                continue;
            }

            // Считывание данных из текущей строки
            int employeeId = Integer.parseInt(record[0].trim());
            String employeeName = record[1].trim();
            String employeeGender = record[2].trim();
            Date birthDate = formatter.parse(record[3].trim());
            String departmentTitle = record[4].trim();
            double income = Double.parseDouble(record[5].trim());

            // Получение или создание департамента
            Department dept = deptDirectory.computeIfAbsent(departmentTitle, Department::new);

            // Создание объекта Employee
            Employee staffMember = new Employee(employeeId, employeeName, employeeGender, dept, income, birthDate);

            // Добавление сотрудника в список
            staffList.add(staffMember);
        }

        csvReader.close(); // Закрытие CSVReader
        return staffList;
    }

    /**
     * Записывает список сотрудников в файл.
     *
     * <p>
     * Для каждого сотрудника в списке:
     * <ul>
     *     <li>Записывается его ID, имя, пол, департамент, зарплата и дата рождения.</li>
     * </ul>
     * </p>
     *
     * @param staffList      Список сотрудников для записи.
     * @param outputFilePath Путь к выходному файлу.
     * @throws IOException Если произошла ошибка при записи в файл.
     */
    public static void writeEmployeesToFile(List<Employee> staffList, String outputFilePath) throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            // Запись заголовка
            writer.write("ID,Имя,Пол,Отдел,Зарплата,Дата рождения");
            writer.newLine();

            // Итерация по списку сотрудников и запись их данных
            for (Employee staff : staffList) {
                String line = String.format(
                        "%d,%s,%s,%s,%.2f,%s",
                        staff.getEmployeeId(),
                        staff.getFullName(),
                        staff.getGenderIdentity(),
                        staff.getWorkDepartment().getDepartmentName(),
                        staff.getMonthlyIncome(),
                        formatter.format(staff.getBirthdate())
                );
                writer.write(line);
                writer.newLine();
            }
        }
    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////

MainTest.java////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс для тестирования функциональности методов в классе {@link Main}.
 */
class MainTest {

    private InputStream fileStream;
    private final String filePath = "foreign_names.csv"; // Путь к тестовому файлу CSV
    private final String outputFilePath = "test_employees_output.txt"; // Имя тестового выходного файла

    /**
     * Инициализация потока для чтения файла перед каждым тестом.
     * Проверяет, что файл доступен для чтения.
     */
    @BeforeEach
    void setUp() {
        fileStream = Main.class.getClassLoader().getResourceAsStream(filePath);
        assertNotNull(fileStream, "Тестовый файл не найден в ресурсах.");
    }

    /**
     * Тестирует метод {@link Main#readCsvAndCreateEmployeeList(InputStream, char, Map)}.
     * Проверяет:
     * <ul>
     *     <li>Список сотрудников успешно создается из CSV-файла.</li>
     *     <li>Данные первого сотрудника в списке соответствуют ожидаемым значениям.</li>
     * </ul>
     *
     * @throws Exception если происходит ошибка при парсинге даты или чтении файла.
     */
    @Test
    void testReadCsvAndCreateEmployeeList() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        char delimiter = ';';

        // Создаем директорию департаментов
        Map<String, Department> deptDirectory = Main.createDeptDirectory();
        // Читаем CSV-файл и создаем список сотрудников
        List<Employee> employeeList = Main.readCsvAndCreateEmployeeList(fileStream, delimiter, deptDirectory);

        // Убедимся, что список сотрудников не пустой
        assertFalse(employeeList.isEmpty(), "Список сотрудников пустой.");

        // Проверяем данные первого сотрудника
        Employee firstEmployee = employeeList.get(0);
        assertEquals(28281, firstEmployee.getEmployeeId(), "ID первого сотрудника не совпадает.");
        assertEquals("Aahan", firstEmployee.getFullName(), "Имя первого сотрудника не совпадает.");
        assertEquals("Male", firstEmployee.getGenderIdentity(), "Пол первого сотрудника не совпадает.");
        assertEquals("I", firstEmployee.getWorkDepartment().getDepartmentName(), "Департамент первого сотрудника не совпадает.");
        assertEquals(4800.0, firstEmployee.getMonthlyIncome(), 0.01, "Зарплата первого сотрудника не совпадает.");
        assertEquals(formatter.parse("15.05.1970"), firstEmployee.getBirthdate(), "Дата рождения первого сотрудника не совпадает.");
    }

    /**
     * Тестирует метод {@link Main#createDeptDirectory()} и создание департаментов.
     * Проверяет:
     * <ul>
     *     <li>Департамент с одинаковым именем создается как один объект.</li>
     *     <li>Разные департаменты создаются как отдельные объекты.</li>
     * </ul>
     */
    @Test
    void testDepartmentCreation() {
        // Создаем директорию департаментов
        Map<String, Department> deptDirectory = Main.createDeptDirectory();

        // Создаем два департамента с одинаковым именем
        Department dept1 = deptDirectory.computeIfAbsent("Finance", Department::new);
        Department dept2 = deptDirectory.computeIfAbsent("Finance", Department::new);

        // Проверяем, что департамент не равен null и оба объекта одинаковы
        assertNotNull(dept1, "Департамент не был создан.");
        assertEquals(dept1, dept2, "Департаменты с одинаковыми названиями должны быть одинаковыми объектами.");

        // Создаем департамент с другим именем
        Department dept3 = deptDirectory.computeIfAbsent("HR", Department::new);

        // Проверяем, что департаменты с разными именами разные объекты
        assertNotEquals(dept1, dept3, "Разные департаменты должны быть разными объектами.");
    }

    /**
     * Тестирует метод {@link Main#writeEmployeesToFile(List, String)}.
     * Проверяет:
     * <ul>
     *     <li>Файл успешно создается и содержит данные сотрудников.</li>
     *     <li>Данные первого сотрудника в файле соответствуют ожидаемым значениям.</li>
     * </ul>
     *
     * @throws Exception если происходит ошибка при чтении файла или записи данных.
     */
    @Test
    void testWriteEmployeesToFile() throws Exception {
        char delimiter = ';';

        // Создаем директорию департаментов и список сотрудников
        Map<String, Department> deptDirectory = Main.createDeptDirectory();
        List<Employee> employeeList = Main.readCsvAndCreateEmployeeList(fileStream, delimiter, deptDirectory);

        // Пишем сотрудников в файл
        Main.writeEmployeesToFile(employeeList, outputFilePath);

        // Проверяем, что файл создан
        File outputFile = new File(outputFilePath);
        assertTrue(outputFile.exists(), "Файл с результатами не был создан.");

        // Проверяем содержимое файла
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            // Пропускаем заголовок
            String header = reader.readLine();
            assertNotNull(header, "Файл пустой, заголовок отсутствует.");
            assertEquals("ID,Имя,Пол,Отдел,Зарплата,Дата рождения", header, "Заголовок файла некорректен.");

            // Проверяем первую строку данных
            String firstLine = reader.readLine();
            assertNotNull(firstLine, "Данные в файле отсутствуют.");

            // Логируем фактическое содержимое первой строки
            System.out.println("Фактическая строка данных: " + firstLine);

            // Ожидаемое значение
            String expectedFirstLine = "28281,Aahan,Male,I,4800,00,15.05.1970";
            assertEquals(expectedFirstLine, firstLine, "Первая строка данных не совпадает с ожидаемой.");
        } finally {
            // Удаляем тестовый файл после проверки
            outputFile.delete();
        }
    }

}


////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
