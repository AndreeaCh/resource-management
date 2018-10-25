package com.deeach.bookshare.api;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

public class GetBooksResponse {
    private List<Book> books;

    public GetBooksResponse(List<Book> booksToPublish) {
        this.books = booksToPublish;
    }

    public GetBooksResponse() {
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public List<Book> getBooks() {
        return books;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("books", books)
                .toString();
    }
}
