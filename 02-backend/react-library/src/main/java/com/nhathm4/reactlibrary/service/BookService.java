package com.nhathm4.reactlibrary.service;

import com.nhathm4.reactlibrary.dao.BookRepository;
import com.nhathm4.reactlibrary.dao.CheckoutRepository;
import com.nhathm4.reactlibrary.dao.HistoryRepository;
import com.nhathm4.reactlibrary.dao.PaymentRepository;
import com.nhathm4.reactlibrary.entity.Book;
import com.nhathm4.reactlibrary.entity.Checkout;
import com.nhathm4.reactlibrary.entity.History;
import com.nhathm4.reactlibrary.entity.Payment;
import com.nhathm4.reactlibrary.responsemodels.ShelfCurrentLoansResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Service
@Transactional
public class BookService {
    private BookRepository bookRepository;

    private CheckoutRepository checkoutRepository;

    private HistoryRepository historyRepository;

    public PaymentRepository paymentRepository;

    public BookService(BookRepository bookRepository, CheckoutRepository checkoutRepository, HistoryRepository historyRepository,
                       PaymentRepository paymentRepository){
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
        this.historyRepository =historyRepository;
        this.paymentRepository = paymentRepository;
    }

    public Book checkoutBook (String userEmail, Long bookId) throws Exception{

        Optional<Book> book = bookRepository.findById(bookId);

        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail,bookId);

        if (!book.isPresent() || validateCheckout != null || book.get().getCopiesAvailable() <= 0){
            throw new Exception("Book doesn't exist or already checked out by user !!! ");
        }

        List<Checkout> currentBookCheckedOut = checkoutRepository.findBookByUserEmail(userEmail);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        boolean bookNeedsReturned = false;

        for (Checkout checkout : currentBookCheckedOut){
            Date d1 = sdf.parse(checkout.getReturnDate());
            Date d2 = sdf.parse(LocalDate.now().toString());

            TimeUnit timeUnit = TimeUnit.DAYS;
            double differenceInTime = timeUnit.convert(d1.getTime()- d2.getTime(), TimeUnit.MILLISECONDS);

            if (differenceInTime < 0){
                bookNeedsReturned = true;
                break;
            }
        }

        Payment userpayment = paymentRepository.findByUserEmail(userEmail);
        if ((userpayment != null && userpayment.getAmount() > 0 ) || (userpayment != null && bookNeedsReturned ) ){
            throw new Exception("Outstanding fees!!!");
        }

        if (userpayment == null ){
            Payment payment = new Payment();
            payment.setAmount(0.00);
            payment.setUserEmail(userEmail);
            paymentRepository.save(payment);
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

    public List<ShelfCurrentLoansResponse> currentLoans(String userEmail) throws Exception {
        List<ShelfCurrentLoansResponse> shelfCurrentLoansResponse  = new ArrayList<>();

        List<Checkout> checkoutList = checkoutRepository.findBookByUserEmail(userEmail);

        List<Long> bookIdList = new ArrayList<>();

        for(Checkout checkout : checkoutList){
            bookIdList.add(checkout.getBookId());
        }

        List<Book> books = bookRepository.findBooksByBookIds(bookIdList);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Book book : books){
            Optional<Checkout> checkout = checkoutList.stream()
                    .filter(x -> x.getBookId() == book.getId()).findFirst();

            if (checkout.isPresent()){
                Date d1= simpleDateFormat.parse(checkout.get().getReturnDate());
                Date d2 = simpleDateFormat.parse(LocalDate.now().toString());
                TimeUnit time = TimeUnit.DAYS;
                long difference_In_Time = time.convert(d1.getTime() - d2.getTime(),
                        TimeUnit.MILLISECONDS);
                shelfCurrentLoansResponse.add(new ShelfCurrentLoansResponse(book, (int) difference_In_Time));
            }
        }
        return shelfCurrentLoansResponse;

    }

    public void returnBook(String userEmail, Long bookId) throws Exception{
        Optional<Book> book = bookRepository.findById(bookId);

        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail,bookId);

        if (!book.isPresent() || validateCheckout == null){
            throw new Exception("This book is not exists !!!");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable()+1);
        bookRepository.save(book.get());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = sdf.parse(validateCheckout.getReturnDate());
        Date d2 = sdf.parse(LocalDate.now().toString());
        TimeUnit timeUnit = TimeUnit.DAYS;
        double differenceInTime = timeUnit.convert(d1.getTime()- d2.getTime(), TimeUnit.MILLISECONDS);
        if (differenceInTime < 0){
            Payment payment = paymentRepository.findByUserEmail(userEmail);
            payment.setAmount(payment.getAmount() + (differenceInTime * -1000));
            paymentRepository.save(payment);
        }

        checkoutRepository.deleteById(validateCheckout.getId());

        History history = new History(
                userEmail,
                validateCheckout.getCheckoutDate(),
                LocalDate.now().toString(),
                book.get().getTitle(),
                book.get().getAuthor(),
                book.get().getDescription(),
                book.get().getImg()
                );
        historyRepository.save(history);

    }

    public void renewBook(String userEmail, Long bookId) throws Exception{
        Optional<Book> book = bookRepository.findById(bookId);

        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail,bookId);

        if (!book.isPresent() || validateCheckout == null){
            throw new Exception("This book is not exists !!!");
        }

        checkoutRepository.deleteById(validateCheckout.getId());
        checkoutBook(userEmail, bookId);
    }

}
