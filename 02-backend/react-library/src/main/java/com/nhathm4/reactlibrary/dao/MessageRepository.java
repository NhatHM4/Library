package com.nhathm4.reactlibrary.dao;

import com.nhathm4.reactlibrary.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {


}
