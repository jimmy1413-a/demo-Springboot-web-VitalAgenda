package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import entity.Roles;
import entity.Usuario;

@Repository
public interface  UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);

    // Buscar usuario por email (para login)
   
    // Buscar usuario por nombre (parcial, sin distinción de mayúsculas)
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);

    // Buscar usuarios por rol
    List<Usuario> findByRol(Roles rol);

    // Verificar si existe un email
    boolean existsByEmail(String email);


}


