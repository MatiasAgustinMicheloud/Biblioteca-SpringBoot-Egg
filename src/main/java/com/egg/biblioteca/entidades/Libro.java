package com.egg.biblioteca.entidades;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
public class Libro {
    @Id
    private Long isbn;

    private String titulo;

    private Integer ejemplares;

    @Temporal(TemporalType.DATE)
    private Date alta;

    @ManyToOne
    private Autor autor;

    @ManyToOne
    private Editorial editorial;
}
