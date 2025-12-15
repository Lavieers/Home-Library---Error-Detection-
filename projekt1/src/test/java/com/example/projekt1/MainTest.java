package com.example.projekt1;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    private static final String TEST_LIBRARY_FILE = "test_library.csv";

    @Test
    public void testSaveAndLoadBooksToFile() throws IOException {
        Main main = new Main();
        main.books.add(new Book("Test Save", "Test Author", "ID456", 2021, "Nauka"));


        main.saveBooksToFile();
        assertTrue(Files.exists(Paths.get(Main.LIBRARY_FILE)), "Library file should exist after saving");


        main.books.clear();
        main.loadBooksFromFile();
        assertEquals(1, main.books.size(), "One book should be loaded from file");
        assertEquals("Test Save", main.books.get(0).getTitle(), "Loaded book title should match");
    }
}