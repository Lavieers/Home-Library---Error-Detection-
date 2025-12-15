package com.example.projekt1;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    ObservableList<Book> books = FXCollections.observableArrayList();
    static final String LIBRARY_FILE = "library.csv";
    static final String HISTORY_FILE = "history.log";
    final List<String> changeLog = new ArrayList<>();
    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    Stage primaryStage;
    Label bookCountLabel;
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showStartWindow();
    }

    private void showStartWindow() {
        Stage startStage = new Stage();
        startStage.initOwner(primaryStage);
        startStage.setTitle("Home Library");

        InputStream iconStream = getClass().getResourceAsStream("/com/example/projekt1/library_icon.png");
        if (iconStream != null) {
            startStage.getIcons().add(new Image(iconStream));
        } else {
            System.out.println("Ikona library_icon.png nie została znaleziona dla okna startowego!");
        }

        BackgroundFill backgroundFill = new BackgroundFill(
                new LinearGradient(0, 0, 0, 1, true, null,
                        new Stop(0, Color.web("#4A90E2")),
                        new Stop(1, Color.web("#63B3ED"))),
                CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);

        VBox startLayout = new VBox(20);
        startLayout.setAlignment(Pos.CENTER);
        startLayout.setBackground(background);
        startLayout.setPadding(new Insets(50));

        Text welcomeLabel = new Text("Witaj w Home Library!");
        welcomeLabel.setFont(Font.font("Arial", 24));
        welcomeLabel.setFill(Color.WHITE);

        Text subtitleLabel = new Text("Zarządzaj swoją kolekcją książek w prosty sposób");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setFill(Color.WHITE);

        Button startButton = new Button("Przejdź do aplikacji");
        startButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #4A90E2; -fx-font-size: 16px; -fx-padding: 10 20; -fx-border-radius: 5; -fx-background-radius: 5;");
        startButton.setOnAction(e -> {
            startStage.close();
            showMainWindow();
        });

        startLayout.getChildren().addAll(welcomeLabel, subtitleLabel, startButton);

        Scene startScene = new Scene(startLayout, 400, 250);
        startStage.setScene(startScene);
        startStage.show();
    }

    private void showMainWindow() {
        InputStream iconStream = getClass().getResourceAsStream("/com/example/projekt1/library_icon.png");
        if (iconStream != null) {
            primaryStage.getIcons().add(new Image(iconStream));
        } else {
            System.out.println("Ikona library_icon.png nie została znaleziona w zasobach!");
        }

        primaryStage.setTitle("Home Library");

        ChoiceBox<String> themeChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("Jasny", "Ciemny"));
        themeChoiceBox.setValue("Jasny");

        Text titleLabel = new Text("Home Library");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setFill(Color.web("#2D3748"));
        titleLabel.setStyle("-fx-effect: dropshadow(gaussian, #4A90E2, 10, 0, 0, 0);");

        bookCountLabel = new Label("Liczba książek: " + books.size());
        bookCountLabel.setFont(Font.font(16));
        bookCountLabel.setAlignment(Pos.CENTER);

        Button libraryButton = new Button("Biblioteka");
        libraryButton.setPrefSize(120, 40);
        libraryButton.setOnAction(event -> new LibraryWindow(primaryStage, books).show());

        Button addBookButton = new Button("Dodaj książkę");
        addBookButton.setPrefSize(120, 40);
        addBookButton.setOnAction(event -> {
            AddBookDialog dialog = new AddBookDialog(primaryStage, FXCollections.observableArrayList("Nauka", "Fantastyka", "Kryminał", "Inne"));
            Book newBook = dialog.showAndWait();
            if (newBook != null) {
                books.add(newBook);
                logChange("Dodano książkę: " + newBook.getTitle());
                saveBooksToFile();
                bookCountLabel.setText("Liczba książek: " + books.size());
                showInfoAlert("Sukces", "Książka dodana!");
            }
        });

        Button statisticsButton = new Button("Statystyki");
        statisticsButton.setPrefSize(120, 40);
        statisticsButton.setOnAction(event -> new StatisticsWindow(primaryStage, books).show());

        HBox navPanel = new HBox(20, libraryButton, addBookButton, statisticsButton);
        navPanel.setAlignment(Pos.CENTER);

        HBox advancedPanel = createAdvancedPanel(themeChoiceBox);

        VBox mainLayout = new VBox(20);
        mainLayout.setId("vbox");
        mainLayout.setPadding(new Insets(30));
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.getChildren().addAll(titleLabel, themeChoiceBox, bookCountLabel, navPanel, advancedPanel);

        Scene scene = new Scene(mainLayout, 700, 400);
        if (getClass().getResource("/com/example/projekt1/styles.css") != null) {
            scene.getStylesheets().add(getClass().getResource("/com/example/projekt1/styles.css").toExternalForm());
        } else {
            System.out.println("Plik styles.css nie został znaleziony!");
        }
        scene.getRoot().getStyleClass().add("light-theme");

        themeChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldTheme, newTheme) -> {
            scene.getRoot().getStyleClass().removeAll("light-theme", "dark-theme");
            if ("Ciemny".equals(newTheme)) {
                scene.getRoot().getStyleClass().add("dark-theme");
                titleLabel.setFill(Color.WHITE);
            } else {
                scene.getRoot().getStyleClass().add("light-theme");
                titleLabel.setFill(Color.web("#2D3748"));
            }
        });

        primaryStage.setScene(scene);
        loadBooksFromFile();
        bookCountLabel.setText("Liczba książek: " + books.size());
        loadHistoryFromFile();
        primaryStage.show();
        Button removeBookButton = new Button("Usuń książkę");
        removeBookButton.setPrefSize(120, 40);
        removeBookButton.setOnAction(event -> {
            RemoveBookDialog dialog = new RemoveBookDialog(primaryStage, books);
            Book bookToRemove = dialog.showAndWait();
            if (bookToRemove != null) {
                books.remove(bookToRemove);
                logChange("Usunięto książkę: " + bookToRemove.getTitle());
                saveBooksToFile();
                bookCountLabel.setText("Liczba książek: " + books.size());
                showInfoAlert("Sukces", "Książka usunięta!");
            }
        });
        navPanel.getChildren().add(removeBookButton);
    }

    private HBox createAdvancedPanel(ChoiceBox<String> themeChoiceBox) {
        Button saveButton = new Button("Zapisz bibliotekę");
        saveButton.setOnAction(event -> showSaveDialog());

        Button loadButton = new Button("Wczytaj bibliotekę");
        loadButton.setOnAction(event -> showLoadDialog());

        Button exportTxtButton = new Button("Eksportuj do TXT");
        exportTxtButton.setOnAction(event -> showExportDialog());

        Button historyButton = new Button("Historia zmian");
        historyButton.setOnAction(event -> showHistoryWindow(primaryStage, themeChoiceBox.getValue()));

        double maxWidth = Math.max(Math.max(saveButton.getPrefWidth(), loadButton.getPrefWidth()),
                Math.max(exportTxtButton.getPrefWidth(), historyButton.getPrefWidth()));
        saveButton.setPrefWidth(maxWidth);
        loadButton.setPrefWidth(maxWidth);
        exportTxtButton.setPrefWidth(maxWidth);
        historyButton.setPrefWidth(maxWidth);
        saveButton.setPrefHeight(40);
        loadButton.setPrefHeight(40);
        exportTxtButton.setPrefHeight(40);
        historyButton.setPrefHeight(40);

        HBox advancedPanel = new HBox(20, saveButton, loadButton, exportTxtButton, historyButton);
        advancedPanel.setAlignment(Pos.CENTER);
        return advancedPanel;
    }

    private void logChange(String message) {
        String logEntry = LocalDateTime.now().format(formatter) + ": " + message;
        changeLog.add(logEntry);
        System.out.println(logEntry);
        saveHistoryToFile();
    }

    private void saveHistoryToFile() {
        try {
            Files.write(Paths.get(HISTORY_FILE), changeLog);
        } catch (IOException e) {
            System.out.println("Błąd zapisywania historii: " + e.getMessage());
        }
    }

    private void loadHistoryFromFile() {
        try {
            if (Files.exists(Paths.get(HISTORY_FILE))) {
                changeLog.addAll(Files.readAllLines(Paths.get(HISTORY_FILE)));
            }
        } catch (IOException e) {
            System.out.println("Błąd wczytywania historii: " + e.getMessage());
        }
    }

    private void showHistoryWindow(Stage owner, String currentTheme) {
        Stage historyStage = new Stage();
        historyStage.setTitle("Historia Zmian");
        historyStage.initOwner(owner);
        InputStream iconStream = getClass().getResourceAsStream("/com/example/projekt1/library_icon.png");
        if (iconStream != null) {
            historyStage.getIcons().add(new Image(iconStream));
        } else {
            System.out.println("Ikona library_icon.png nie została znaleziona dla okna historii!");
        }

        TextArea historyTextArea = new TextArea();
        historyTextArea.setEditable(false);
        historyTextArea.setWrapText(true);
        historyTextArea.setPrefHeight(300);
        historyTextArea.setPrefWidth(500);
        historyTextArea.setText(String.join("\n", changeLog));

        VBox historyLayout = new VBox(10);
        historyLayout.setId("history-vbox");
        historyLayout.getChildren().add(historyTextArea);

        Scene historyScene = new Scene(historyLayout, 550, 350);
        if (getClass().getResource("/com/example/projekt1/styles.css") != null) {
            historyScene.getStylesheets().add(getClass().getResource("/com/example/projekt1/styles.css").toExternalForm());
        } else {
            System.out.println("Plik styles.css nie został znaleziony!");
        }
        historyScene.getRoot().getStyleClass().add("Ciemny".equals(currentTheme) ? "dark-theme" : "light-theme");

        historyStage.setScene(historyScene);
        historyStage.show();
    }

    private void configureAlert(Alert alert) {
        InputStream iconStream = getClass().getResourceAsStream("/com/example/projekt1/library_icon.png");
        if (iconStream != null) {
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(iconStream));
        } else {
            System.out.println("Ikona library_icon.png nie została znaleziona dla okna dialogowego!");
        }

        if (getClass().getResource("/com/example/projekt1/styles.css") != null) {
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/com/example/projekt1/styles.css").toExternalForm());
        } else {
            System.out.println("Plik styles.css nie został znaleziony!");
        }

        // Aplikowanie odpowiedniego stylu w zależności od trybu
        if (primaryStage.getScene() != null && primaryStage.getScene().getRoot().getStyleClass().contains("dark-theme")) {
            alert.getDialogPane().getStyleClass().add("dark-theme");
        } else {
            alert.getDialogPane().getStyleClass().add("light-theme");
        }
    }
    private void showSaveDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Zapisz bibliotekę");
        alert.setHeaderText("Czy chcesz zapisać bibliotekę?");
        alert.setContentText("Plik zostanie zapisany jako " + LIBRARY_FILE + ".");

        configureAlert(alert);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                saveBooksToFile();
                showInfoAlert("Sukces", "Biblioteka została zapisana do pliku.");
            }
        });
    }

    private void showLoadDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Wczytaj bibliotekę");
        alert.setHeaderText("Czy chcesz wczytać bibliotekę?");
        alert.setContentText("Obecna biblioteka zostanie zastąpiona danymi z pliku " + LIBRARY_FILE + ".");

        configureAlert(alert);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                loadBooksFromFile();
                bookCountLabel.setText("Liczba książek: " + books.size());
                showInfoAlert("Sukces", "Biblioteka została wczytana z pliku.");
            }
        });
    }

    private void showExportDialog() {
        if (books.isEmpty()) {
            showInfoAlert("Informacja", "Brak książek do eksportu.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Zapisz plik TXT");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pliki tekstowe", "*.txt"));
        fileChooser.setInitialFileName("library.txt");
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file == null) return;

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("Lista Książek w Bibliotece");
            writer.println();
            writer.printf("%-40s %-30s %-10s %-10s %-20s%n", "Tytuł", "Autor", "ID", "Rok", "Kategoria");
            writer.println("-".repeat(110));

            for (Book book : books) {
                writer.printf("%-40s %-30s %-10s %-10d %-20s%n",
                        truncate(book.getTitle(), 40),
                        truncate(book.getAuthor(), 30),
                        truncate(book.getId(), 10),
                        book.getYear(),
                        truncate(book.getCategory(), 20));
            }

            showInfoAlert("Sukces", "Lista książek została wyeksportowana do pliku " + file.getAbsolutePath());
        } catch (IOException e) {
            showErrorAlert("Błąd eksportu", "Nie udało się wyeksportować do TXT: " + e.getMessage());
        }
    }

    private String truncate(String text, int maxLength) {
        if (text == null) return "";
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }

    void saveBooksToFile() {
        try {
            StringBuilder csvContent = new StringBuilder();
            csvContent.append("title,author,id,year,category\n");
            for (Book book : books) {
                csvContent.append(String.format("\"%s\",\"%s\",\"%s\",%d,\"%s\"\n",
                        book.getTitle().replace("\"", "\"\""),
                        book.getAuthor().replace("\"", "\"\""),
                        book.getId().replace("\"", "\"\""),
                        book.getYear(),
                        book.getCategory().replace("\"", "\"\"")));
            }
            Files.writeString(Paths.get(LIBRARY_FILE), csvContent.toString());
        } catch (IOException e) {
            showErrorAlert("Błąd zapisu", "Nie udało się zapisać biblioteki: " + e.getMessage());
        }
    }

    void loadBooksFromFile() {
        try {
            if (Files.exists(Paths.get(LIBRARY_FILE))) {
                List<String> lines = Files.readAllLines(Paths.get(LIBRARY_FILE));
                books.clear();
                for (String line : lines.subList(1, lines.size())) {
                    String[] parts = parseCsvLine(line);
                    if (parts.length == 5) {
                        books.add(new Book(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), parts[4]));
                    } else if (parts.length == 4) {
                        books.add(new Book(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), "Inne"));
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            showInfoAlert("Informacja", "Brak pliku biblioteki lub błąd wczytywania: " + e.getMessage());
        }
    }

    private String[] parseCsvLine(String line) {
        String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].replaceAll("^\"|\"$", "").replace("\"\"", "\"");
        }
        return parts;
    }

    private void applyAlertStyles(Alert alert) {
        if (getClass().getResource("/com/example/projekt1/styles.css") != null) {
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/com/example/projekt1/styles.css").toExternalForm());
        } else {
            System.out.println("Plik styles.css nie został znaleziony!");
        }
        if (primaryStage.getScene() != null && primaryStage.getScene().getRoot().getStyleClass().contains("dark-theme")) {
            alert.getDialogPane().getStyleClass().add("dark-theme");
        } else {
            alert.getDialogPane().getStyleClass().add("light-theme");
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        applyAlertStyles(alert);
        alert.showAndWait();
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        applyAlertStyles(alert);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}