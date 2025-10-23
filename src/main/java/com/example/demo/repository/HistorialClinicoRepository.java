package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.HistorialClinico;
import com.example.demo.entity.Medico;
import com.example.demo.entity.Paciente;

@Repository
public interface  HistorialClinicoRepository extends JpaRepository<HistorialClinico, Long> {
        // Buscar historial por paciente
    List<HistorialClinico> findByPaciente(Paciente paciente);

    // Buscar historial por médico
    List<HistorialClinico> findByMedico(Medico medico);

    // Buscar historial por fecha exacta
    List<HistorialClinico> findByFecha(LocalDate fecha);

    // Buscar historial entre fechas
    List<HistorialClinico> findByFechaBetween(LocalDate desde, LocalDate hasta);

    // Buscar historial por paciente y médico
    List<HistorialClinico> findByPacienteAndMedico(Paciente paciente, Medico medico);

}