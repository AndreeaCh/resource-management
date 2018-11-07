package com.resource.management.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;

public class Book {
    @Id
    private final String title;
    private final String author;
    private final String publisher;
    private final String genre;
    private final BookOwner owner;

    public Book(final String title, String author, String publisher, String genre, BookOwner owner) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.genre = genre;
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getGenre() {
        return genre;
    }

    public BookOwner getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("title", title)
                .append("author", author)
                .append("publisher", publisher)
                .append("genre", genre)
                .append("owner", owner)
                .toString();
    }
}
