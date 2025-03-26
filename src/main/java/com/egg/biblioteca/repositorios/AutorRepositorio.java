package com.egg.biblioteca.repositorios;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.egg.biblioteca.entidades.Autor;

@Repository
public interface AutorRepositorio extends JpaRepository<Autor, UUID> {

    public Optional<Autor> findByNombre(String nombre);

}
