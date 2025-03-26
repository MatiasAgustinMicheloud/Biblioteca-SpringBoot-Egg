package com.egg.biblioteca.servicios;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.EditorialRepositorio;

@Service
public class EditorialServicio {
    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional
    public Editorial crearEditorial(String nombre) throws MiException {
        validar(nombre);

        Editorial editorial = new Editorial();

        editorial.setNombre(nombre);

        return editorialRepositorio.save(editorial);
    }

    @Transactional(readOnly = true)
    public List<Editorial> listarEditoriales() {
        return editorialRepositorio.findAll();
    }

    @Transactional
    public Editorial modificarEditorial(UUID id, String nombre) throws MiException {
        validar(nombre);

        Optional<Editorial> respuesta = editorialRepositorio.findById(id);

        if (!respuesta.isPresent()) {
            throw new MiException("No se encontró la editorial con el ID proporcionado.");
        }

        Editorial editorial = respuesta.get();

        editorial.setNombre(nombre);

        return editorialRepositorio.save(editorial);
    }

    private void validar(String nombre) throws MiException {
        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre no puede ser nulo o estar vacío!");
        }

        if (!nombre.matches("^[0-9a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*$")) {
            throw new MiException("El nombre solo puede contener letras, números o espacios.");
        }

        Optional<Editorial> editorialExistente = editorialRepositorio.findByNombre(nombre);
        if (editorialExistente.isPresent()) {
            throw new MiException("Ya existe una editorial con ese nombre.");
        }
    }

    @Transactional(readOnly = true)
    public Editorial getOne(UUID id) {
        return editorialRepositorio.getReferenceById(id);
    }
}
