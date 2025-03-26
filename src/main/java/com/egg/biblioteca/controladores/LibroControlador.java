package com.egg.biblioteca.controladores;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.AutorServicio;
import com.egg.biblioteca.servicios.EditorialServicio;
import com.egg.biblioteca.servicios.LibroServicio;

@Controller
@RequestMapping("/libro")
public class LibroControlador {

    @Autowired
    private LibroServicio libroServicio;

    @Autowired
    private AutorServicio autorServicio;

    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/registrar")
    public String registrar(ModelMap modelo) {
        List<Autor> autores = autorServicio.listarAutores();
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);

        return "libro_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam(required = false) Long isbn,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Integer ejemplares,
            @RequestParam(required = false) UUID idAutor,
            @RequestParam(required = false) UUID idEditorial,
            ModelMap modelo,
            RedirectAttributes redirectAttributes) {

        try {

            libroServicio.crearLibro(isbn, titulo, ejemplares, idAutor, idEditorial);
            redirectAttributes.addFlashAttribute("exito", "Libro registrado con exito!");
            return "redirect:/";

        } catch (MiException e) {
            modelo.put("isbn", isbn);
            modelo.put("titulo", titulo);
            modelo.put("ejemplares", ejemplares);
            modelo.put("idAutor", idAutor);
            modelo.put("idEditorial", idEditorial);

            List<Autor> autores = autorServicio.listarAutores();
            List<Editorial> editoriales = editorialServicio.listarEditoriales();
            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);

            modelo.put("error", e.getMessage());
            return "libro_form.html";
        }
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        modelo.addAttribute("libros", libroServicio.listarLibros());
        return "libro_list.html";
    }

    @GetMapping("/modificar/{isbn}")
    public String modificar(@PathVariable Long isbn, ModelMap modelo) {
        modelo.addAttribute("libro", libroServicio.getOne(isbn));
        modelo.addAttribute("autores", autorServicio.listarAutores());
        modelo.addAttribute("editoriales", editorialServicio.listarEditoriales());
        return "libro_modificar.html";
    }

    @PostMapping("/modificar/{isbn}")
    public String modificar(@PathVariable Long isbn, String titulo, Integer ejemplares, UUID idAutor, UUID idEditorial,
            RedirectAttributes redirectAttributes, ModelMap modelo) {
        try {

            libroServicio.modificarLibro(isbn, titulo, ejemplares, idAutor, idEditorial);
            redirectAttributes.addFlashAttribute("exito", "Libro actualizado con exito!");
            return "redirect:../lista";

        } catch (MiException e) {

            modelo.put("error", e.getMessage());
            modelo.addAttribute("libro", libroServicio.getOne(isbn));
            modelo.addAttribute("autores", autorServicio.listarAutores());
            modelo.addAttribute("editoriales", editorialServicio.listarEditoriales());
            return "libro_modificar.html";
        }
    }

}
