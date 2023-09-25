package com.nhathm4.reactlibrary.dao;

import com.nhathm4.reactlibrary.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}
