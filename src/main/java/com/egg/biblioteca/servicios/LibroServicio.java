package com.egg.biblioteca.servicios;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.entidades.Libro;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.AutorRepositorio;
import com.egg.biblioteca.repositorios.EditorialRepositorio;
import com.egg.biblioteca.repositorios.LibroRepositorio;

@Service
public class LibroServicio {
    @Autowired
    private LibroRepositorio libroRepositorio;

    @Autowired
    private AutorRepositorio autorRepositorio;
    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional
    public Libro crearLibro(Long isbn, String titulo, Integer ejemplares, UUID autorId, UUID editorialId)
            throws MiException {
        validar(isbn, titulo, ejemplares, autorId, editorialId);

        Autor autor = autorRepositorio.findById(autorId).get();
        Editorial editorial = editorialRepositorio.findById(editorialId).get(); // usar optional

        Libro libro = new Libro();

        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setEjemplares(ejemplares);
        libro.setAlta(new Date());
        libro.setAutor(autor);
        libro.setEditorial(editorial);

        return libroRepositorio.save(libro);

    }

    @Transactional(readOnly = true)
    public List<Libro> listarLibros() {
        return libroRepositorio.findAll();
    }

    @Transactional
    public Libro modificarLibro(Long isbn, String titulo, Integer ejemplares, UUID autorId, UUID editorialId)
            throws MiException {
        validar(isbn, titulo, ejemplares, autorId, editorialId);

        Optional<Libro> respuestaLibro = libroRepositorio.findById(isbn);
        Optional<Autor> respuestaAutor = autorRepositorio.findById(autorId);

        Editorial editorial = editorialRepositorio.findById(editorialId)
                .orElseThrow(() -> new MiException("La editorial no existe.")); // Otro metodo de validar la existencia.

        if (!respuestaLibro.isPresent()) {
            throw new MiException("No se encontro un libro con el ISBN proporcionado.");
        }

        if (!respuestaAutor.isPresent()) {
            throw new MiException("El autor no existe.");
        }

        Libro libro = respuestaLibro.get();
        Autor autor = respuestaAutor.get();

        libro.setTitulo(titulo);
        libro.setEjemplares(ejemplares);
        libro.setAutor(autor);
        libro.setEditorial(editorial);

        return libroRepositorio.save(libro);
    }

    private void validar(Long isbn, String titulo, Integer ejemplares, UUID autorId, UUID editorialId)
            throws MiException {

        if (isbn == null || isbn <= 0) {
            throw new MiException("El ISBN debe ser un número válido (mayor a cero).");
        }

        if (libroRepositorio.existsById(isbn)) {
            throw new MiException("Ya existe un libro con ese ISBN.");
        }

        if (titulo == null || titulo.trim().isEmpty()) {
            throw new MiException("El título no puede ser nulo o estar vacío.");
        }

        if (ejemplares == null || ejemplares < 0) {
            throw new MiException("La cantidad de ejemplares debe ser un número positivo.");
        }

        if (autorId == null || !autorRepositorio.existsById(autorId)) {
            throw new MiException("El autor no existe.");
        }

        if (editorialId == null || !editorialRepositorio.existsById(editorialId)) {
            throw new MiException("La editorial no existe.");
        }
    }

}
