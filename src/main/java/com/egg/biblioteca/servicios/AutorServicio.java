package com.egg.biblioteca.servicios;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.AutorRepositorio;

@Service
public class AutorServicio {
    @Autowired
    private AutorRepositorio autorRepositorio;

    @Transactional
    public Autor crearAutor(String nombre) throws MiException {
        validar(nombre);

        Autor autor = new Autor();

        autor.setNombre(nombre);

        return autorRepositorio.save(autor);
    }

    @Transactional(readOnly = true)
    public List<Autor> listarAutores() {
        return autorRepositorio.findAll();
    }

    @Transactional
    public Autor modificarAutor(UUID id, String nombre) throws MiException {
        validar(nombre);

        // Tambien se puede usar de esta forma:
        // Autor autor = autorRepositorio.findById(id)
        // .orElseThrow(() -> new MiException("No se encontró el autor con el ID
        // proporcionado."));

        Optional<Autor> respuesta = autorRepositorio.findById(id);

        if (!respuesta.isPresent()) {
            throw new MiException("No se encontró el autor con el ID proporcionado.");
        }

        Autor autor = respuesta.get();

        autor.setNombre(nombre);

        return autorRepositorio.save(autor);
    }

    private void validar(String nombre) throws MiException {
        // Validar que el nombre no sea nulo ni vacío
        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre no puede ser nulo o estar vacío.");
        }

        // Validar formato del nombre (solo letras y espacios)
        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*$")) {
            throw new MiException("El nombre solo puede contener letras y espacios.");
        }

        // Validar que el nombre no exista en la base de datos
        Optional<Autor> autorExistente = autorRepositorio.findByNombre(nombre);
        if (autorExistente.isPresent()) {
            throw new MiException("Ya existe un autor con ese nombre.");
        }
    }

    @Transactional(readOnly = true)
    public Autor getOne(UUID id) {
        return autorRepositorio.getReferenceById(id);
    }

}
