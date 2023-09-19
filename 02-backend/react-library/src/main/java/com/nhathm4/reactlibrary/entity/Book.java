package com.nhathm4.reactlibrary.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name= "Book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="description")
    private String description;


}
