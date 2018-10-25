package com.deeach.bookshare.controller;

import com.deeach.bookshare.api.AddBookRequest;
import com.deeach.bookshare.api.AddBookResponse;
import com.deeach.bookshare.data.Book;
import com.deeach.bookshare.data.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class AddBookController {

    @Autowired
    private BookRepository repository;

    @MessageMapping("/addBook")
    @SendTo("/topic/books")
    public AddBookResponse books(final AddBookRequest addBookRequest) throws Exception {
        repository.insert(new Book(addBookRequest.getTitle(), null, "Default", "SCI-FI", null));
        return new AddBookResponse("OK");
    }

}