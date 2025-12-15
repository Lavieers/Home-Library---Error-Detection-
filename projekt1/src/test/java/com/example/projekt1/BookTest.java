package com.example.projekt1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    @Test
    public void testBookCreation() {
        Book book = new Book("Title", "Author", "ID123", 2020, "Category");
        assertEquals("Title", book.getTitle(), "Book title should match constructor");
        assertEquals("Author", book.getAuthor(), "Book author should match constructor");
        assertEquals("ID123", book.getId(), "Book ID should match constructor");
        assertEquals(2020, book.getYear(), "Book year should match constructor");
        assertEquals("Category", book.getCategory(), "Book category should match constructor");
    }

    @Test
    public void testToString() {
        Book book = new Book("Title", "Author", "ID123", 2020, "Category");
        String expected = "Title";
        assertEquals(expected, book.toString(), "toString should return the title");
    }
}