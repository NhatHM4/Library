package com.nhathm4.reactlibrary.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name ="messages")
public class Message {

    public Message(){}

    public Message(String title, String question){
        this.title = title;
        this.question = question;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "title")
    private String title;

    @Column(name = "question")
    private String question;

    @Column(name = "admin_email")
    private String admin_email;

    @Column(name = "response")
    private String response;

    @Column(name = "closed")
    private boolean closed;
}
