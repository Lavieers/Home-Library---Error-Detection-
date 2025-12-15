package com.example.projekt1;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Book {
    private final SimpleStringProperty title = new SimpleStringProperty("");
    private final SimpleStringProperty author = new SimpleStringProperty("");
    private final SimpleStringProperty id = new SimpleStringProperty("");
    private final SimpleIntegerProperty year = new SimpleIntegerProperty(0);
    private final SimpleStringProperty category = new SimpleStringProperty("");

    public Book(String title, String author, String id, int year, String category) {
        this.title.set(title);
        this.author.set(author);
        this.id.set(id);
        this.year.set(year);
        this.category.set(category);
    }

    public String getTitle() { return title.get(); }
    public SimpleStringProperty titleProperty() { return title; }
    public String getAuthor() { return author.get(); }
    public SimpleStringProperty authorProperty() { return author; }
    public String getId() { return id.get(); }
    public SimpleStringProperty idProperty() { return id; }
    public int getYear() { return year.get(); }
    public SimpleIntegerProperty yearProperty() { return year; }
    public String getCategory() { return category.get(); }
    public SimpleStringProperty categoryProperty() { return category; }

    @Override
    public String toString() {
        return title.get();
    }
}