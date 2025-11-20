package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entity.Roles;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Web methods
    @GetMapping("")
    public String listarUsuariosWeb(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "usuario/usuario-list";
    }

    @GetMapping("/form")
    public String mostrarFormularioUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario/form";
    }

    @GetMapping("/{id}/edit")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            return "usuario/form";
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/{id}")
    public String verDetalleUsuario(@PathVariable Long id, Model model) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            return "usuario/usuario-detail";
        }
        return "redirect:/usuarios";
    }

    @PostMapping("")
    public String crearUsuarioWeb(@ModelAttribute Usuario usuario) {
        usuarioRepository.save(usuario);
        return "redirect:/usuarios";
    }

    @PutMapping("/{id}")
    public String actualizarUsuarioWeb(@PathVariable Long id, @ModelAttribute Usuario usuario) {
        usuario.setId(id);
        usuarioRepository.save(usuario);
        return "redirect:/usuarios";
    }

    // API methods
    @GetMapping("/api")
    @ResponseBody
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // Obtener usuario por ID
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear nuevo usuario
    @PostMapping("/api")
    @ResponseBody
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Actualizar usuario
    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario datosActualizados) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombre(datosActualizados.getNombre());
            usuario.setEmail(datosActualizados.getEmail());
            usuario.setContrasena(datosActualizados.getContrasena());
            return ResponseEntity.ok(usuarioRepository.save(usuario));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Eliminar usuario
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Buscar por nombre (parcial, sin distinción de mayúsculas)
    @GetMapping("/api/nombre/{nombre}")
    @ResponseBody
    public List<Usuario> buscarPorNombre(@PathVariable String nombre) {
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Buscar por rol
    @GetMapping("/api/rol/{rol}")
    @ResponseBody
    public List<Usuario> buscarPorRol(@PathVariable Roles rol) {
        return usuarioRepository.findByRol(rol);
    }

    // Verificar si existe un email
    @GetMapping("/api/existe-email/{email}")
    @ResponseBody
    public boolean existeEmail(@PathVariable String email) {
        return usuarioRepository.existsByEmail(email);
    }

    // Buscar usuario por email (para login)
    @GetMapping("/api/email/{email}")
    @ResponseBody
    public ResponseEntity<Usuario> buscarPorEmail(@PathVariable String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }
}