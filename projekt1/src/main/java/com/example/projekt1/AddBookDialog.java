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

public class AddBookDialog {
    private final Stage dialogStage;
    private Book result;
    private TextField titleField;
    private TextField authorField;
    private TextField idField;
    private TextField yearField;
    private ChoiceBox<String> categoryChoice;
    private Button addButton;
    private Button cancelButton;

    public AddBookDialog(Stage owner, ObservableList<String> categories) {
        dialogStage = new Stage();
        dialogStage.initOwner(owner);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        InputStream iconStream = getClass().getResourceAsStream("/com/example/projekt1/library_icon.png");
        if (iconStream != null) {
            dialogStage.getIcons().add(new Image(iconStream));
        } else {
            System.out.println("Ikona library_icon.png nie została znaleziona dla okna dodawania!");
        }
        createDialog(categories);
    }

    private void createDialog(ObservableList<String> categories) {
        dialogStage.setTitle("Dodaj książkę");

        Label titleLabel = new Label("Tytuł:");
        titleField = new TextField();
        titleField.setId("titleField");
        titleField.setPrefWidth(300);
        Label authorLabel = new Label("Autor:");
        authorField = new TextField();
        authorField.setId("authorField");
        authorField.setPrefWidth(300);
        Label idLabel = new Label("ID:");
        idField = new TextField(generateId());
        idField.setId("idField");
        idField.setPrefWidth(300);
        idField.setEditable(false);
        Label yearLabel = new Label("Rok wydania:");
        yearField = new TextField();
        yearField.setId("yearField");
        yearField.setPrefWidth(300);
        Label categoryLabel = new Label("Kategoria:");
        categoryChoice = new ChoiceBox<>(categories);
        categoryChoice.setId("categoryCombo");
        categoryChoice.setValue(categories.get(0));
        categoryChoice.setPrefWidth(300);

        addButton = new Button("Dodaj");
        addButton.setId("addButton");
        cancelButton = new Button("Anuluj");
        cancelButton.setId("cancelButton");

        addButton.setOnAction(e -> handleAddButton());
        cancelButton.setOnAction(e -> dialogStage.close());

        HBox buttonBox = new HBox(15, addButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(25);
        layout.setPadding(new Insets(40));
        layout.getChildren().addAll(
                titleLabel, titleField,
                authorLabel, authorField,
                idLabel, idField,
                yearLabel, yearField,
                categoryLabel, categoryChoice,
                buttonBox
        );

        Scene scene = new Scene(layout, 500, 700);
        if (getClass().getResource("/com/example/projekt1/styles.css") != null) {
            scene.getStylesheets().add(getClass().getResource("/com/example/projekt1/styles.css").toExternalForm());
        } else {
            System.out.println("Plik styles.css nie został znaleziony!");
        }
        Stage owner = (Stage) dialogStage.getOwner();
        if (owner != null && owner.getScene() != null && owner.getScene().getRoot().getStyleClass().contains("dark-theme")) {
            scene.getRoot().getStyleClass().add("dark-theme");
        } else {
            scene.getRoot().getStyleClass().add("light-theme");
        }

        dialogStage.setScene(scene);
    }

    private void handleAddButton() {
        try {
            String yearText = yearField.getText().trim();
            if (yearText.isEmpty()) {
                showErrorAlert("Błąd", "Proszę podać rok wydania.");
                return;
            }

            int year = Integer.parseInt(yearText);
            if (year <= 0) {
                showErrorAlert("Błąd", "Rok musi być liczbą dodatnią.");
                return;
            }

            if (titleField.getText().isEmpty() || authorField.getText().isEmpty()) {
                showErrorAlert("Błąd", "Proszę wypełnić wszystkie pola.");
                return;
            }

            result = new Book(
                    titleField.getText(),
                    authorField.getText(),
                    idField.getText(),
                    year,
                    categoryChoice.getValue()
            );
            dialogStage.close();
        } catch (NumberFormatException ex) {
            showErrorAlert("Błąd", "Rok musi być poprawną liczbą.");
        }
    }

    private String generateId() {
        return "ID" + System.currentTimeMillis();
    }

    public Book getResult() {
        return result;
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        if (getClass().getResource("/com/example/projekt1/styles.css") != null) {
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/com/example/projekt1/styles.css").toExternalForm());
        } else {
            System.out.println("Plik styles.css nie został znaleziony!");
        }
        Stage owner = (Stage) dialogStage.getOwner();
        if (owner != null && owner.getScene() != null && owner.getScene().getRoot().getStyleClass().contains("dark-theme")) {
            alert.getDialogPane().getStyleClass().add("dark-theme");
        } else {
            alert.getDialogPane().getStyleClass().add("light-theme");
        }
        alert.showAndWait();
    }
    public Book showAndWait() {
        dialogStage.showAndWait();
        return getResult();
    }

    public Stage getDialogStage() {
        return dialogStage;
    }
}