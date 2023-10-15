package com.nhathm4.reactlibrary.entity;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name= "Book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="title")
    private String title;

    @Column(name ="author")
    private String author;

    @Column(name ="description")
    private String description;

    @Column(name ="copies")
    private int copies;

    @Column(name ="copiesAvailable")
    private int copiesAvailable;

    @Column(name ="category")
    private String category;

    @Column(name ="img")
    private String img;

}
