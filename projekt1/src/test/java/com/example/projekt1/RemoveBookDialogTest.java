package com.example.projekt1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class RemoveBookDialogTest extends ApplicationTest {

    private RemoveBookDialog dialog;
    private ObservableList<Book> books;

    @Override
    public void start(Stage stage) {
        books = FXCollections.observableArrayList(
                new Book("Book1", "Author1", "ID001", 2020, "Nauka"),
                new Book("Book2", "Author2", "ID002", 2021, "Fantastyka")
        );
        dialog = new RemoveBookDialog(stage, books);
    }

    @Test
    public void testSelectAndRemoveBook() {
        interact(() -> {
            dialog.getDialogStage().show();
        });
        targetWindow(dialog.getDialogStage());
        clickOn("#bookCombo").clickOn("Book1");
        clickOn("#removeButton");
        // Oczekiwanie na zamknięcie dialogu
        long startTime = System.currentTimeMillis();
        while (dialog.getDialogStage().isShowing() && System.currentTimeMillis() - startTime < 5000) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        Book selectedBook = dialog.getResult();
        assertNotNull(selectedBook, "A book should be selected for removal");
        assertEquals("Book1", selectedBook.getTitle(), "Selected book should be Book1");
    }

    @Test
    public void testEmptyBookList() {
        AtomicReference<RemoveBookDialog> emptyDialog = new AtomicReference<>();
        interact(() -> {
            Stage stage = new Stage();
            emptyDialog.set(new RemoveBookDialog(stage, FXCollections.observableArrayList()));
            emptyDialog.get().getDialogStage().show();
        });
        targetWindow(emptyDialog.get().getDialogStage());
        clickOn("#cancelButton");

        long startTime = System.currentTimeMillis();
        while (emptyDialog.get().getDialogStage().isShowing() && System.currentTimeMillis() - startTime < 5000) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        Book selectedBook = emptyDialog.get().getResult();
        assertNull(selectedBook, "No book should be selected when the list is empty");
    }
}