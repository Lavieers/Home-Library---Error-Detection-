package com.example.projekt1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;

public class LibraryWindow {
    private final Stage stage;
    private final ObservableList<Book> books;
    private TableView<Book> tableView;
    private TextField titleField;
    private TextField authorField;
    private TextField yearField;
    private TextField categoryField;

    public LibraryWindow(Stage owner, ObservableList<Book> books) {
        this.stage = new Stage();
        this.stage.initOwner(owner);
        this.books = books;
        InputStream iconStream = getClass().getResourceAsStream("/com/example/projekt1/library_icon.png");
        if (iconStream != null) {
            this.stage.getIcons().add(new Image(iconStream));
        } else {
            System.out.println("Ikona library_icon.png nie została znaleziona dla okna biblioteki!");
        }
        createWindow();
    }

    private void createWindow() {
        stage.setTitle("Biblioteka");

        tableView = new TableView<>();
        TableColumn<Book, String> titleCol = new TableColumn<>("Tytuł");
        titleCol.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        titleCol.setMinWidth(150);
        titleCol.setPrefWidth(200);
        TableColumn<Book, String> authorCol = new TableColumn<>("Autor");
        authorCol.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        authorCol.setMinWidth(120);
        authorCol.setPrefWidth(150);
        TableColumn<Book, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        idCol.setMinWidth(80);
        idCol.setPrefWidth(100);
        TableColumn<Book, Integer> yearCol = new TableColumn<>("Rok");
        yearCol.setCellValueFactory(cellData -> cellData.getValue().yearProperty().asObject());
        yearCol.setMinWidth(60);
        yearCol.setPrefWidth(80);
        TableColumn<Book, String> categoryCol = new TableColumn<>("Kategoria");
        categoryCol.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        categoryCol.setMinWidth(100);

        tableView.getColumns().addAll(titleCol, authorCol, idCol, yearCol, categoryCol);
        tableView.setItems(books);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        categoryCol.prefWidthProperty().bind(
                tableView.widthProperty()
                        .subtract(titleCol.widthProperty())
                        .subtract(authorCol.widthProperty())
                        .subtract(idCol.widthProperty())
                        .subtract(yearCol.widthProperty())
                        .subtract(20)
        );

        Label titleLabel = new Label("Tytuł:");
        titleField = new TextField();
        titleField.setId("titleField");
        Label authorLabel = new Label("Autor:");
        authorField = new TextField();
        authorField.setId("authorField");
        Label yearLabel = new Label("Rok:");
        yearField = new TextField();
        yearField.setId("yearField");
        Label categoryLabel = new Label("Kategoria:");
        categoryField = new TextField();
        categoryField.setId("categoryField");
        Button searchButton = new Button("Wyszukaj");
        searchButton.setId("searchButton");
        Button clearFiltersButton = new Button("Anuluj filtry");
        clearFiltersButton.setId("clearButton");
        clearFiltersButton.setOnAction(event -> clearFilters());

        HBox searchForm = new HBox(15);
        searchForm.setAlignment(Pos.CENTER);
        searchForm.setPadding(new Insets(10));
        searchForm.getChildren().addAll(
                new VBox(5, titleLabel, titleField),
                new VBox(5, authorLabel, authorField),
                new VBox(5, yearLabel, yearField),
                new VBox(5, categoryLabel, categoryField),
                new VBox(5, searchButton, clearFiltersButton)
        );

        searchButton.setOnAction(event -> {
            String title = titleField.getText().trim().toLowerCase();
            String author = authorField.getText().trim().toLowerCase();
            String yearText = yearField.getText().trim();
            String category = categoryField.getText().trim().toLowerCase();

            ObservableList<Book> filteredBooks = FXCollections.observableArrayList();
            for (Book book : books) {
                boolean matches = true;

                if (!title.isEmpty() && !book.getTitle().toLowerCase().contains(title)) {
                    matches = false;
                }
                if (!author.isEmpty() && !book.getAuthor().toLowerCase().contains(author)) {
                    matches = false;
                }
                if (!yearText.isEmpty()) {
                    try {
                        int year = Integer.parseInt(yearText);
                        if (book.getYear() != year) {
                            matches = false;
                        }
                    } catch (NumberFormatException e) {
                        matches = false;
                    }
                }
                if (!category.isEmpty() && !book.getCategory().toLowerCase().contains(category)) {
                    matches = false;
                }

                if (matches) {
                    filteredBooks.add(book);
                }
            }

            tableView.setItems(filteredBooks);
        });

        VBox layout = new VBox(20);
        layout.setId("library-vbox");
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(searchForm, tableView);

        Scene scene = new Scene(layout, 800, 500);
        scene.getStylesheets().add(getClass().getResource("/com/example/projekt1/styles.css").toExternalForm());
        Stage owner = (Stage) stage.getOwner();
        if (owner != null && owner.getScene() != null && owner.getScene().getRoot().getStyleClass().contains("dark-theme")) {
            scene.getRoot().getStyleClass().add("dark-theme");
        } else {
            scene.getRoot().getStyleClass().add("light-theme");
        }

        stage.setScene(scene);
    }

    private void clearFilters() {
        titleField.clear();
        authorField.clear();
        yearField.clear();
        categoryField.clear();
        tableView.setItems(books);
    }

    public void show() {
        stage.show();
    }

    public TableView<Book> getTableView() {
        return tableView;
    }

    public Stage getStage() {
        return stage;
    }
}