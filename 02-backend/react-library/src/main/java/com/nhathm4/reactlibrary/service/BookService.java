package com.nhathm4.reactlibrary.service;

import com.nhathm4.reactlibrary.dao.BookRepository;
import com.nhathm4.reactlibrary.dao.CheckoutRepository;
import com.nhathm4.reactlibrary.entity.Book;
import com.nhathm4.reactlibrary.entity.Checkout;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class BookService {
    private BookRepository bookRepository;

    private CheckoutRepository checkoutRepository;

    public BookService(BookRepository bookRepository, CheckoutRepository checkoutRepository){
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
    }

    public Book checkoutBook (String userEmail, Long bookId) throws Exception{

        Optional<Book> book = bookRepository.findById(bookId);

        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail,bookId);

        if (!book.isPresent() || validateCheckout != null || book.get().getCopiesAvailable() <= 0){
            throw new Exception("Book doesn't exist or already checked out by user !!! ");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
        bookRepository.save(book.get());


        Checkout checkout = new Checkout(userEmail,
                LocalDate.now().toString(),
                LocalDate.now().plusDays(7).toString(),
                book.get().getId());

        checkoutRepository.save(checkout);

        return book.get();
    }

    public Boolean checkoutBookByUser(String userEmail,Long bookId){
        Checkout checkout = checkoutRepository.findByUserEmailAndBookId(userEmail,bookId);
        if (checkout != null) {
            return true;
        }
        return false;
    }

    public int getNumberCurrentLoans(String userEmail){
        return checkoutRepository.findBookByUserEmail(userEmail).size();
    }
}
