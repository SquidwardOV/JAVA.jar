pom.xml/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>org.example</groupId>
  <artifactId>Laba4</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>Laba4</name>
  <url>http://maven.apache.org</url>

  <repositories>
    <repository>
      <id>central</id>
      <url>https://repo.maven.apache.org/maven2</url>
    </repository>
  </repositories>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.apache.commons</groupId>
      <artifactId>commons-lang3</artifactId>
      <version>3.12.0</version>
    </dependency>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter</artifactId>
      <version>RELEASE</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
</project>

  
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

Person.java/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

import java.time.LocalDate;

/**
 * Класс, представляющий человека (Person).
 */
public class Person {
    private long id;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private Division division;
    private double salary;

    /**
     * Конструктор для создания объекта {@link Person}.
     *
     * @param id        уникальный идентификатор человека.
     * @param name      имя человека.
     * @param gender    пол человека.
     * @param birthDate дата рождения человека.
     * @param division  подразделение, к которому относится человек.
     * @param salary    заработная плата человека.
     */
    public Person(long id, String name, String gender, LocalDate birthDate, Division division, double salary) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.division = division;
        this.salary = salary;
    }

    /**
     * Возвращает уникальный идентификатор человека.
     *
     * @return идентификатор человека.
     */
    public long getId() {
        return id;
    }

    /**
     * Возвращает имя человека.
     *
     * @return имя человека.
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает пол человека.
     *
     * @return пол человека.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Возвращает дату рождения человека.
     *
     * @return дата рождения человека.
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Возвращает подразделение человека.
     *
     * @return подразделение человека.
     */
    public Division getDivision() {
        return division;
    }

    /**
     * Возвращает заработную плату человека.
     *
     * @return заработная плата человека.
     */
    public double getSalary() {
        return salary;
    }

    /**
     * Переопределение метода toString для удобного вывода объекта.
     *
     * @return строковое представление объекта {@link Person}.
     */
    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", birthDate=" + birthDate +
                ", division=" + division.getName() +
                ", salary=" + salary +
                '}';
    }
}

  
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  

Division.java///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

/**
 * Класс, представляющий подразделение (Division).
 */
public class Division {
    private static long idCounter = 1; // Для генерации уникальных ID

    private long id;
    private String name;

    /**
     * Конструктор для создания объекта {@link Division}.
     *
     * @param name название подразделения.
     */
    public Division(String name) {
        this.id = idCounter++;
        this.name = name;
    }

    /**
     * Возвращает уникальный идентификатор подразделения.
     *
     * @return идентификатор подразделения.
     */
    public long getId() {
        return id;
    }

    /**
     * Возвращает название подразделения.
     *
     * @return название подразделения.
     */
    public String getName() {
        return name;
    }

    /**
     * Переопределение метода equals для сравнения объектов {@link Division}.
     *
     * @param obj объект для сравнения.
     * @return true, если объекты равны; false в противном случае.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Division)) return false;
        Division other = (Division) obj;
        return this.name.equals(other.name);
    }

    /**
     * Переопределение метода hashCode для использования объекта в коллекциях.
     *
     * @return хэш-код объекта.
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CsvReaderApp.java///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Основной класс приложения для чтения данных из CSV-файла.
 * Преобразует данные из CSV в список объектов {@link Person}.
 */
public class CsvReaderApp {

    /**
     * Точка входа в программу.
     *
     * @param args аргументы командной строки.
     */
    public static void main(String[] args) {
        String csvFilePath = "foreign_names.csv";
        char separator = ';';
        String outputFilePath = "people_output.txt"; 

        List<Person> people = readPeopleFromCsv(csvFilePath, separator);

        // Запись списка в текстовый файл для отладки
        writePeopleToFile(people, outputFilePath);
    }

    /**
     * Читает данные из CSV-файла и преобразует их в список объектов {@link Person}.
     *
     * @param csvFilePath путь к CSV-файлу.
     * @param separator   символ разделителя в файле.
     * @return список объектов {@link Person}.
     */
    public static List<Person> readPeopleFromCsv(String csvFilePath, char separator) {
        List<Person> people = new ArrayList<>();
        Map<String, Division> divisionMap = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        try (Reader reader = new FileReader(csvFilePath)) {
            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(separator)
                    .build();

            try (CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withCSVParser(parser)
                    .build()) {

                String[] nextLine;
                boolean isHeader = true;

                while ((nextLine = csvReader.readNext()) != null) {
                    if (isHeader) {
                        isHeader = false;
                        continue;
                    }

                    if (nextLine.length != 6) {
                        System.err.println("Неверное количество полей в строке: " + Arrays.toString(nextLine));
                        continue;
                    }

                    try {
                        long id = Long.parseLong(nextLine[0].trim());
                        String name = nextLine[1].trim();
                        String gender = nextLine[2].trim();
                        LocalDate birthDate = LocalDate.parse(nextLine[3].trim(), formatter);
                        String divisionName = nextLine[4].trim();
                        double salary = Double.parseDouble(nextLine[5].trim());

                        Division division = divisionMap.computeIfAbsent(divisionName, Division::new);

                        Person person = new Person(id, name, gender, birthDate, division, salary);
                        people.add(person);
                    } catch (NumberFormatException | DateTimeParseException e) {
                        System.err.println("Ошибка при парсинге строки: " + Arrays.toString(nextLine));
                    }
                }
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println("Ошибка при чтении файла: " + csvFilePath);
            e.printStackTrace();
        }

        return people;
    }

    /**
     * Записывает список объектов {@link Person} в текстовый файл.
     *
     * @param people         список объектов {@link Person}.
     * @param outputFilePath путь к выходному текстовому файлу.
     */
    public static void writePeopleToFile(List<Person> people, String outputFilePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            for (Person person : people) {
                writer.write(person.toString());
                writer.newLine();
            }
            System.out.println("Список people успешно сформирован и записан в файл: " + outputFilePath + " для его проверки");
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + outputFilePath);
            e.printStackTrace();
        }
    }

}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CsvReaderAppTest.java//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package org.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовый класс для {@link CsvReaderApp}.
 * Проверяет корректность чтения и парсинга CSV-файлов в объекты {@link Person}.
 */
public class CsvReaderAppTest {

    /**
     * Временная директория для создания временных CSV-файлов в тестах.
     */
    @TempDir
    Path tempDir;

    /**
     * Создает временный CSV-файл с заданным содержимым.
     *
     * @param fileName    имя файла.
     * @param fileContent содержимое файла.
     * @return путь к созданному файлу.
     * @throws IOException если возникает ошибка при создании файла.
     */
    private Path createTempCsvFile(String fileName, String fileContent) throws IOException {
        Path filePath = tempDir.resolve(fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            writer.write(fileContent);
        }
        return filePath;
    }

    /**
     * Тестирует успешное чтение корректного CSV-файла.
     */
    @Test
    @DisplayName("Тест успешного чтения корректного CSV-файла")
    void testReadPeopleFromCsv_Success() throws IOException {
        String csvContent = "id;name;gender;birthDate;division;salary\n" +
                "1;John Doe;M;15.04.1990;Engineering;75000\n" +
                "2;Jane Smith;F;22.08.1985;Marketing;68000";

        Path csvFile = createTempCsvFile("valid_people.csv", csvContent);

        List<Person> people = CsvReaderApp.readPeopleFromCsv(csvFile.toString(), ';');

        assertEquals(2, people.size(), "Должно быть прочитано 2 записи");

        Person person1 = people.get(0);
        assertEquals(1L, person1.getId());
        assertEquals("John Doe", person1.getName());
        assertEquals("M", person1.getGender());
        assertEquals(LocalDate.of(1990, 4, 15), person1.getBirthDate());
        assertEquals("Engineering", person1.getDivision().getName());
        assertEquals(75000.0, person1.getSalary());

        Person person2 = people.get(1);
        assertEquals(2L, person2.getId());
        assertEquals("Jane Smith", person2.getName());
        assertEquals("F", person2.getGender());
        assertEquals(LocalDate.of(1985, 8, 22), person2.getBirthDate());
        assertEquals("Marketing", person2.getDivision().getName());
        assertEquals(68000.0, person2.getSalary());
    }

    /**
     * Тестирует поведение при наличии строк с неверным количеством полей.
     */
    @Test
    @DisplayName("Тест обработки строк с неверным количеством полей")
    void testReadPeopleFromCsv_InvalidFieldCount() throws IOException {
        String csvContent = "id;name;gender;birthDate;division;salary\n" +
                "1;John Doe;M;15.04.1990;Engineering;75000\n" +
                "2;Jane Smith;F;22.08.1985;Marketing"; // Недостаточно полей

        Path csvFile = createTempCsvFile("invalid_field_count.csv", csvContent);

        List<Person> people = CsvReaderApp.readPeopleFromCsv(csvFile.toString(), ';');

        assertEquals(1, people.size(), "Должна быть прочитана только 1 корректная запись");

        Person person1 = people.get(0);
        assertEquals(1L, person1.getId());
        assertEquals("John Doe", person1.getName());
        assertEquals("M", person1.getGender());
        assertEquals(LocalDate.of(1990, 4, 15), person1.getBirthDate());
        assertEquals("Engineering", person1.getDivision().getName());
        assertEquals(75000.0, person1.getSalary());
    }

    /**
     * Тестирует поведение при наличии строк с неверным форматом числа.
     */
    @Test
    @DisplayName("Тест обработки строк с неверным форматом числа")
    void testReadPeopleFromCsv_InvalidNumberFormat() throws IOException {
        String csvContent = "id;name;gender;birthDate;division;salary\n" +
                "1;John Doe;M;15.04.1990;Engineering;seventy-five thousand\n" + // Неверный формат salary
                "2;Jane Smith;F;22.08.1985;Marketing;68000";

        Path csvFile = createTempCsvFile("invalid_number_format.csv", csvContent);

        List<Person> people = CsvReaderApp.readPeopleFromCsv(csvFile.toString(), ';');

        assertEquals(1, people.size(), "Должна быть прочитана только 1 корректная запись");

        Person person2 = people.get(0);
        assertEquals(2L, person2.getId());
        assertEquals("Jane Smith", person2.getName());
        assertEquals("F", person2.getGender());
        assertEquals(LocalDate.of(1985, 8, 22), person2.getBirthDate());
        assertEquals("Marketing", person2.getDivision().getName());
        assertEquals(68000.0, person2.getSalary());
    }

    /**
     * Тестирует поведение при наличии строк с неверным форматом даты.
     */
    @Test
    @DisplayName("Тест обработки строк с неверным форматом даты")
    void testReadPeopleFromCsv_InvalidDateFormat() throws IOException {
        String csvContent = "id;name;gender;birthDate;division;salary\n" +
                "1;John Doe;M;1990-04-15;Engineering;75000\n" + // Неверный формат даты
                "2;Jane Smith;F;22.08.1985;Marketing;68000";

        Path csvFile = createTempCsvFile("invalid_date_format.csv", csvContent);

        List<Person> people = CsvReaderApp.readPeopleFromCsv(csvFile.toString(), ';');

        assertEquals(1, people.size(), "Должна быть прочитана только 1 корректная запись");

        Person person2 = people.get(0);
        assertEquals(2L, person2.getId());
        assertEquals("Jane Smith", person2.getName());
        assertEquals("F", person2.getGender());
        assertEquals(LocalDate.of(1985, 8, 22), person2.getBirthDate());
        assertEquals("Marketing", person2.getDivision().getName());
        assertEquals(68000.0, person2.getSalary());
    }

    /**
     * Тестирует поведение при пустом CSV-файле.
     */
    @Test
    @DisplayName("Тест обработки пустого CSV-файла")
    void testReadPeopleFromCsv_EmptyFile() throws IOException {
        String csvContent = "";

        Path csvFile = createTempCsvFile("empty.csv", csvContent);

        List<Person> people = CsvReaderApp.readPeopleFromCsv(csvFile.toString(), ';');

        assertEquals(0, people.size(), "Должен быть пустой список");
    }

    /**
     * Тестирует поведение при отсутствии CSV-файла.
     */
    @Test
    @DisplayName("Тест обработки отсутствующего CSV-файла")
    void testReadPeopleFromCsv_FileNotFound() {
        String csvFilePath = tempDir.resolve("non_existent.csv").toString();

        List<Person> people = CsvReaderApp.readPeopleFromCsv(csvFilePath, ';');

        assertEquals(0, people.size(), "Должен быть пустой список при отсутствии файла");
    }

    /**
     * Тестирует поведение при наличии только заголовка в CSV-файле.
     */
    @Test
    @DisplayName("Тест обработки CSV-файла с только заголовком")
    void testReadPeopleFromCsv_OnlyHeader() throws IOException {
        String csvContent = "id;name;gender;birthDate;division;salary\n";

        Path csvFile = createTempCsvFile("only_header.csv", csvContent);

        List<Person> people = CsvReaderApp.readPeopleFromCsv(csvFile.toString(), ';');

        assertEquals(0, people.size(), "Должен быть пустой список при наличии только заголовка");
    }

    /**
     * Тестирует поведение при наличии нескольких разделителей в поле.
     */
    @Test
    @DisplayName("Тест обработки строк с разделителями внутри полей")
    void testReadPeopleFromCsv_FieldsWithSeparators() throws IOException {
        String csvContent = "id;name;gender;birthDate;division;salary\n" +
                "1;Doe; John;M;15.04.1990;Engineering;75000\n" + // Некорректное количество полей
                "2;Jane Smith;F;22.08.1985;Marketing;68000";

        Path csvFile = createTempCsvFile("fields_with_separators.csv", csvContent);

        List<Person> people = CsvReaderApp.readPeopleFromCsv(csvFile.toString(), ';');

        assertEquals(1, people.size(), "Должна быть прочитана только 1 корректная запись");
        Person person2 = people.get(0);
        assertEquals(2L, person2.getId());
        assertEquals("Jane Smith", person2.getName());
        assertEquals("F", person2.getGender());
        assertEquals(LocalDate.of(1985, 8, 22), person2.getBirthDate());
        assertEquals("Marketing", person2.getDivision().getName());
        assertEquals(68000.0, person2.getSalary());
    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
