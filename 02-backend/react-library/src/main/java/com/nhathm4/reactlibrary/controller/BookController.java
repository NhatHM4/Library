package com.nhathm4.reactlibrary.controller;

import com.nhathm4.reactlibrary.entity.Book;
import com.nhathm4.reactlibrary.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @PutMapping("/secure/checkout")
    public Book checkoutBook(@RequestParam Long id ) throws Exception {
        String userEmail = "testuser@email.com";
        return bookService.checkoutBook(userEmail,id);
    }

    @GetMapping("/secure/ischeckedout/byuser")
    public boolean isCheckoutBookByUser(@RequestParam Long bookId){
        String userEmail = "testuser@email.com";
        return bookService.checkoutBookByUser(userEmail, bookId);
    }

    @GetMapping("/secure/currentloans/count")
    public int currentloans(){
        String userEmail = "testuser@email.com";
        return  bookService.getNumberCurrentLoans(userEmail);
    }
}
