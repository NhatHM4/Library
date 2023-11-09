package com.nhathm4.reactlibrary.dao;

import com.nhathm4.reactlibrary.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;

public interface HistoryRepository extends JpaRepository<History, Long> {

    Page<History> findBooksByUserEmail(@RequestParam String userEmail, Pageable pageable);
}
