package com.example.catalog.persistence.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Tag {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "tag_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "value")
    private String value;
}
