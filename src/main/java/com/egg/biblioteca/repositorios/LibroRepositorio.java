package com.egg.biblioteca.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.egg.biblioteca.entidades.Libro;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, Long> {
    public Optional<Libro> findByTitulo(String titulo);

    public Optional<List<Libro>> findByAutorNombre(String nombre);
}
