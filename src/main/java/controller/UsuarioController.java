package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import entity.Usuario;
import repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 🟢 Listar todos los usuarios
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // 🔵 Obtener usuario por ID
    @GetMapping("/{id}")
    public Optional<Usuario> obtenerUsuario(@PathVariable Long id) {
        return usuarioRepository.findById(id);
    }

    // 🟡 Crear nuevo usuario
    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // 🟠 Actualizar usuario existente
    @PutMapping("/{id}")
    public Usuario actualizarUsuario(@PathVariable Long id, @RequestBody Usuario actualizado) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombre(actualizado.getNombre());
            usuario.setEmail(actualizado.getEmail());
            usuario.setContraseña(actualizado.getContraseña());
            usuario.setRol(actualizado.getRol());
            return usuarioRepository.save(usuario);
        }).orElseGet(() -> {
            actualizado.setId(id);
            return usuarioRepository.save(actualizado);
        });
    }

    // 🔴 Eliminar usuario
    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
    }

    // 🔐 Autenticación básica (solo para pruebas)
    @PostMapping("/login")
    public boolean autenticar(@RequestBody Usuario login) {
        Usuario usuario = usuarioRepository.findByEmail(login.getEmail());
        return usuario != null && usuario.autenticar(login.getEmail(), login.getContraseña());
    }
}