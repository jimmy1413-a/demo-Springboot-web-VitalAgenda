package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Roles;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Obtener todos los usuarios
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear nuevo usuario
    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario datosActualizados) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombre(datosActualizados.getNombre());
            usuario.setEmail(datosActualizados.getEmail());
            usuario.setContrasena(datosActualizados.getContrasena());
            return ResponseEntity.ok(usuarioRepository.save(usuario));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Buscar por nombre (parcial, sin distinción de mayúsculas)
    @GetMapping("/nombre/{nombre}")
    public List<Usuario> buscarPorNombre(@PathVariable String nombre) {
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Buscar por rol
    @GetMapping("/rol/{rol}")
    public List<Usuario> buscarPorRol(@PathVariable Roles rol) {
        return usuarioRepository.findByRol(rol);
    }

    // Verificar si existe un email
    @GetMapping("/existe-email/{email}")
    public boolean existeEmail(@PathVariable String email) {
        return usuarioRepository.existsByEmail(email);
    }

    // Buscar usuario por email (para login)
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> buscarPorEmail(@PathVariable String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }
}