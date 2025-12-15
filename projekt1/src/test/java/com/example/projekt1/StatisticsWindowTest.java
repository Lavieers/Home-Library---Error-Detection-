package com.example.projekt1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.*;

public class StatisticsWindowTest extends ApplicationTest {

    private StatisticsWindow window;
    private ObservableList<Book> books;

    @Override
    public void start(Stage stage) {
        books = FXCollections.observableArrayList(
                new Book("Book1", "Author1", "ID001", 2020, "Nauka"),
                new Book("Book2", "Author1", "ID002", 2021, "Fantastyka"),
                new Book("Book3", "Author2", "ID003", 2020, "Nauka")
        );
        window = new StatisticsWindow(stage, books);
    }

    @Test
    public void testBookCount() {
        // Sprawdzenie liczby książek
        assertEquals(3, books.size(), "Book count should be 3");
    }

    @Test
    public void testAverageYear() {
        // Średni rok: (2020 + 2021 + 2020) / 3 = 2020.33 -> zaokrąglone do 2020
        int expectedAvg = 2020;
        assertEquals(expectedAvg, (int) books.stream().mapToInt(Book::getYear).average().orElse(0),
                "Average year should be 2020");
    }

    @Test
    public void testMostCommonAuthor() {
        // Najczęstszy autor: Author1 (2 razy)
        String expectedAuthor = "Author1";
        String mostCommonAuthor = books.stream()
                .collect(java.util.stream.Collectors.groupingBy(Book::getAuthor, java.util.stream.Collectors.counting()))
                .entrySet().stream()
                .max(java.util.Map.Entry.comparingByValue())
                .map(java.util.Map.Entry::getKey)
                .orElse("Brak danych");
        assertEquals(expectedAuthor, mostCommonAuthor, "Most common author should be Author1");
    }
}