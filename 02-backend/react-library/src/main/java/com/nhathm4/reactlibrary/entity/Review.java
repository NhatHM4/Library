package com.nhathm4.reactlibrary.entity;

import lombok.Data;

import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name="review")
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "date")
    @CreationTimestamp
    private Date date;

    @Column(name = "rating")
    private double rating;


    @Column(name = "book_id")
    private Long bookId;


    @Column(name = "review_decription")
    private String reviewDecription;

}
