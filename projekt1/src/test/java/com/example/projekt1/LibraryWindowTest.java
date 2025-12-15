package com.example.projekt1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.*;

public class LibraryWindowTest extends ApplicationTest {

    private LibraryWindow window;
    private ObservableList<Book> books;

    @Override
    public void start(Stage stage) {
        books = FXCollections.observableArrayList(
                new Book("Book1", "Author1", "ID001", 2020, "Nauka"),
                new Book("Book2", "Author2", "ID002", 2021, "Fantastyka")
        );
        window = new LibraryWindow(stage, books);
        window.show();
    }

    @Test
    public void testInitialBookCount() {
        assertEquals(2, books.size(), "Initial book count should be 2");
    }

    @Test
    public void testSearchByTitle() {
        targetWindow(window.getStage());
        clickOn("#titleField").write("Book1");
        clickOn("#searchButton");
        sleep(500);
        assertEquals(1, window.getTableView().getItems().size(), "TableView should show only one book after search");
    }

    @Test
    public void testClearFilters() {
        targetWindow(window.getStage());
        clickOn("#titleField").write("Book1");
        clickOn("#searchButton");
        sleep(500);
        clickOn("#clearButton");
        sleep(500);
        assertEquals(2, window.getTableView().getItems().size(), "TableView should show all books after clearing filters");
    }
}