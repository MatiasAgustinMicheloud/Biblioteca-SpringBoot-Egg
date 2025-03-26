package com.egg.biblioteca.controladores;

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

import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.EditorialServicio;

@Controller
@RequestMapping("/editorial")
public class EditorialControlador {
    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/registrar")
    public String registrar() {
        return "editorial_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, ModelMap modelo, RedirectAttributes redirectAttributes) {
        try {
            editorialServicio.crearEditorial(nombre);
            redirectAttributes.addFlashAttribute("exito", "Editorial registrada con exito!");
            return "redirect:/";
        } catch (MiException e) {
            modelo.put("nombre", nombre);
            modelo.put("error", e.getMessage());
            return "editorial_form.html";
        }
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        modelo.addAttribute("editoriales", editorialServicio.listarEditoriales());
        return "editorial_list.html";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable UUID id, ModelMap modelo) {
        modelo.put("editorial", editorialServicio.getOne(id));
        return "editorial_modificar.html";
    }

    // No hace falta agregar el @RequestParam porque spring lo hace por defecto si
    // coiciden el nombre de los parametros con los del html.
    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable UUID id, String nombre, ModelMap modelo,
            RedirectAttributes redirectAttributes) {
        try {
            editorialServicio.modificarEditorial(id, nombre);
            redirectAttributes.addFlashAttribute("exito", "Editorial actualizada con exito!");
            return "redirect:../lista";
        } catch (MiException e) {
            modelo.put("error", e.getMessage());
            modelo.put("editorial", editorialServicio.getOne(id));
            return "editorial_modificar.html";
        }
    }
}
