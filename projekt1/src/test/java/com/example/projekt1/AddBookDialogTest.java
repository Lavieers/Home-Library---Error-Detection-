package com.example.projekt1;

import javafx.collections.FXCollections;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class AddBookDialogTest {

    private AddBookDialog dialog;

    @Start
    public void start(Stage stage) {
        dialog = new AddBookDialog(stage, FXCollections.observableArrayList("Nauka", "Fantastyka"));
        dialog.getDialogStage().show();
    }

    @Test
    public void testValidBookCreation(FxRobot robot) {
        robot.clickOn("#titleField").write("Test Book");
        robot.clickOn("#authorField").write("Test Author");
        robot.clickOn("#yearField").write("2022");
        robot.clickOn("#categoryCombo").clickOn("Nauka");
        robot.clickOn("#addButton");

        WaitForAsyncUtils.waitForFxEvents();

        Book book = dialog.getResult();
        assertNotNull(book, "Book should be created");
        assertEquals("Test Book", book.getTitle(), "Title should match input");
        assertEquals("Test Author", book.getAuthor(), "Author should match input");
        assertNotNull(book.getId(), "ID should be set");
        assertTrue(book.getId().startsWith("ID"), "ID should start with 'ID'");
        assertEquals(2022, book.getYear(), "Year should match input");
        assertEquals("Nauka", book.getCategory(), "Category should match input");
    }

    @Test
    public void testInvalidYearInput(FxRobot robot) {
        robot.clickOn("#titleField").write("Test Book");
        robot.clickOn("#authorField").write("Test Author");
        robot.clickOn("#idField").write("ID789");
        robot.clickOn("#yearField").write("invalid");
        robot.clickOn("#categoryCombo").clickOn("Nauka");
        robot.clickOn("#addButton");

        WaitForAsyncUtils.waitForFxEvents();
        Book book = dialog.getResult();
        assertNull(book, "Book should not be created with invalid year");
    }

    @Test
    public void testEmptyFields(FxRobot robot) {
        robot.clickOn("#addButton");
        WaitForAsyncUtils.waitForFxEvents();
        Book book = dialog.getResult();
        assertNull(book, "Book should not be created with empty fields");
    }
}