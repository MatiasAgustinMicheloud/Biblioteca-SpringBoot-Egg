package com.egg.biblioteca.entidades;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Editorial {
    @Id
    @GeneratedValue
    private UUID id;

    private String nombre;
}
