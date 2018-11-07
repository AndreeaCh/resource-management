package com.resource.management.controller;

import com.resource.management.api.GetBooksRequest;
import com.resource.management.api.GetBooksResponse;
import com.resource.management.data.Book;
import com.resource.management.data.BookRepository;
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
        List<com.resource.management.api.Book> booksToPublish = convertBooks(books);
        return new GetBooksResponse(booksToPublish);
    }

    private List<com.resource.management.api.Book> convertBooks(List<Book> books) {
        return books.stream()
                .map(b -> new com.resource.management.api.Book(b.getTitle(), b.getAuthor(), b.getPublisher(), b.getGenre()))
                .collect(Collectors.toList());
    }
}