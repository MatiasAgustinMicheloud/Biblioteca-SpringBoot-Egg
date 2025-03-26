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
import com.egg.biblioteca.servicios.AutorServicio;

@Controller
@RequestMapping("/autor")
public class AutorControlador {
    @Autowired
    private AutorServicio autorServicio;

    @GetMapping("/registrar")
    public String registrar() {
        return "autor_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, ModelMap modelo, RedirectAttributes redirectAttributes) {
        try {
            autorServicio.crearAutor(nombre);

            // el redirectAttributes permite mandar mensaje de exito si usamos un redirect
            // en el return, a diferencia del modelMap que no lo permite.
            redirectAttributes.addFlashAttribute("exito", "Autor registrado con exito!");

            return "redirect:/";
        } catch (MiException e) {
            modelo.put("nombre", nombre);
            modelo.put("error", e.getMessage());
            return "autor_form.html";
        }
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        modelo.addAttribute("autores", autorServicio.listarAutores());
        return "autor_list.html";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable UUID id, ModelMap modelo) {
        modelo.put("autor", autorServicio.getOne(id));
        return "autor_modificar.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable UUID id, String nombre, ModelMap modelo,
            RedirectAttributes redirectAttributes) {
        try {
            autorServicio.modificarAutor(id, nombre);
            // mensaje de exito con redirectAttributes
            redirectAttributes.addFlashAttribute("exito", "Autor actualizado con exito!");
            return "redirect:../lista";
        } catch (MiException e) {
            modelo.put("error", e.getMessage());
            modelo.put("autor", autorServicio.getOne(id));
            return "autor_modificar.html";
        }
    }

}
