package com.example.demo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    // Buscar pacientes por nombre (heredado de Usuario)
    List<Paciente> findByNombreContainingIgnoreCase(String nombre);

    // Buscar pacientes por teléfono
    List<Paciente> findByTelefono(String telefono);

    // Buscar pacientes por dirección
    List<Paciente> findByDireccionContainingIgnoreCase(String direccion);

}