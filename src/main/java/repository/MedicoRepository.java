package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import entity.Medico;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    // Buscar médicos por especialidad
    List<Medico> findByEspecialidad(String especialidad);

    // Buscar médicos por nombre (heredado de Usuario)
    List<Medico> findByNombreContainingIgnoreCase(String nombre);

    // Buscar médicos por consultorio
    List<Medico> findByConsultorio(String consultorio);

}