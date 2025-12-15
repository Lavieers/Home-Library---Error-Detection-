package com.example.projekt1;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.InputStream;

public class RemoveBookDialog {
    private final Stage dialogStage;
    private Book result;
    private ComboBox<Book> bookComboBox;
    private Button removeButton;
    private Button cancelButton;

    public RemoveBookDialog(Stage owner, ObservableList<Book> books) {
        dialogStage = new Stage();
        dialogStage.initOwner(owner);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        InputStream iconStream = getClass().getResourceAsStream("/com/example/projekt1/library_icon.png");
        if (iconStream != null) {
            dialogStage.getIcons().add(new Image(iconStream));
        } else {
            System.out.println("Ikona library_icon.png nie została znaleziona dla okna usuwania!");
        }
        createDialog(books);
    }

    private void createDialog(ObservableList<Book> books) {
        dialogStage.setTitle("Usuń książkę");

        bookComboBox = new ComboBox<>(books);
        bookComboBox.setId("bookCombo");
        bookComboBox.setPrefWidth(300);
        if (!books.isEmpty()) {
            bookComboBox.getSelectionModel().selectFirst();
        }

        removeButton = new Button("Usuń");
        removeButton.setId("removeButton");
        cancelButton = new Button("Anuluj");
        cancelButton.setId("cancelButton");

        removeButton.setOnAction(e -> {
            result = bookComboBox.getValue();
            if (result != null) {
                dialogStage.close();
            }
        });

        cancelButton.setOnAction(e -> dialogStage.close());

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(new Label("Wybierz książkę do usunięcia:"), bookComboBox, new HBox(10, removeButton, cancelButton));

        Scene scene = new Scene(layout, 400, 200);
        scene.getStylesheets().add(getClass().getResource("/com/example/projekt1/styles.css").toExternalForm());
        Stage owner = (Stage) dialogStage.getOwner();
        if (owner != null && owner.getScene() != null && owner.getScene().getRoot().getStyleClass().contains("dark-theme")) {
            scene.getRoot().getStyleClass().add("dark-theme");
        } else {
            scene.getRoot().getStyleClass().add("light-theme");
        }

        dialogStage.setScene(scene);
    }

    public Book showAndWait() {
        result = null;
        dialogStage.showAndWait();
        return result;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public Book getResult() {
        return result;
    }
}