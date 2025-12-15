package com.example.projekt1;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.*;

public class HelloControllerTest extends ApplicationTest {

    private HelloController controller;
    private Label welcomeText;

    @Override
    public void start(Stage stage) {
        controller = new HelloController();
        welcomeText = new Label();

        controller.welcomeText = welcomeText;
    }

    @Test
    public void testOnHelloButtonClick() {

        controller.onHelloButtonClick();
        assertEquals("Welcome to JavaFX Application!", welcomeText.getText(),
                "Label text should be updated after button click");
    }
}