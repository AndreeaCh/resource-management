package com.deeach.bookshare.controller;

import com.deeach.bookshare.api.GetBooksRequest;
import com.deeach.bookshare.api.GetBooksResponse;
import com.deeach.bookshare.data.Book;
import com.deeach.bookshare.data.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GetBookController {

    @Autowired
    private BookRepository repository;

    @MessageMapping("/getBook")
    @SendTo("/topic/books")
    public GetBooksResponse books(final GetBooksRequest getBooksRequest) throws Exception {
        List<Book> books = repository.findByTitle(getBooksRequest.getTitle());
        List<com.deeach.bookshare.api.Book> booksToPublish = convertBooks(books);
        return new GetBooksResponse(booksToPublish);
    }

    private List<com.deeach.bookshare.api.Book> convertBooks(List<Book> books) {
        return books.stream()
                .map(b -> new com.deeach.bookshare.api.Book(b.getTitle(), b.getAuthor(), b.getPublisher(), b.getGenre()))
                .collect(Collectors.toList());
    }
}