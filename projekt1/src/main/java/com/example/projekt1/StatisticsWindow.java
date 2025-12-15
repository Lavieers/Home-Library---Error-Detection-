package com.example.projekt1;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class StatisticsWindow {
    private final Stage stage;

    public StatisticsWindow(Stage owner, ObservableList<Book> books) {
        stage = new Stage();
        stage.initOwner(owner);
        InputStream iconStream = getClass().getResourceAsStream("/com/example/projekt1/library_icon.png");
        if (iconStream != null) {
            stage.getIcons().add(new Image(iconStream));
        } else {
            System.out.println("Ikona library_icon.png nie została znaleziona dla okna statystyk!");
        }
        createWindow(books);
    }

    private void createWindow(ObservableList<Book> books) {
        stage.setTitle("Statystyki biblioteki");

        int bookCount = books.size();
        int avgYear = bookCount > 0 ? (int) books.stream().mapToInt(Book::getYear).average().orElse(0) : 0;
        Map<String, Long> categoryCount = new HashMap<>();
        books.forEach(book -> categoryCount.merge(book.getCategory(), 1L, Long::sum));
        String mostCommonAuthor = books.stream()
                .collect(java.util.stream.Collectors.groupingBy(Book::getAuthor, java.util.stream.Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Brak danych");

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(
                new Label("Liczba książek: " + bookCount),
                new Label("Średni rok wydania: " + avgYear),
                new Label("Najczęściej występujący autor: " + mostCommonAuthor),
                new Label("Liczba książek w kategoriach:")
        );

        if (!categoryCount.isEmpty()) {
            layout.getChildren().addAll(
                    categoryCount.entrySet().stream()
                            .map(entry -> new Label(entry.getKey() + ": " + entry.getValue()))
                            .toArray(Label[]::new)
            );
        } else {
            layout.getChildren().add(new Label("Brak danych o kategoriach."));
        }

        Button closeButton = new Button("Zamknij");
        closeButton.setOnAction(e -> stage.close());
        layout.getChildren().add(closeButton);

        Scene scene = new Scene(layout, 400, 300);
        scene.getStylesheets().add(getClass().getResource("/com/example/projekt1/styles.css").toExternalForm());
        Stage owner = (Stage) stage.getOwner();
        if (owner != null && owner.getScene() != null && owner.getScene().getRoot().getStyleClass().contains("dark-theme")) {
            scene.getRoot().getStyleClass().add("dark-theme");
        } else {
            scene.getRoot().getStyleClass().add("light-theme");
        }

        stage.setScene(scene);
        stage.show();
    }

    public void show() {
        stage.show();
    }
}