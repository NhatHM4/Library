package com.nhathm4.reactlibrary.dao;

import com.nhathm4.reactlibrary.entity.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CheckoutRepository extends JpaRepository<Checkout, Long> {

     Checkout findByUserEmailAndBookId(String userEmail, Long bookId);

     List<Checkout> findBookByUserEmail(String userEmail);

     @Modifying
     @Query("delete  from  Checkout where bookId in :book_id")
     void deleteAllByBookId(@Param("book_id") Long bookId);
}
