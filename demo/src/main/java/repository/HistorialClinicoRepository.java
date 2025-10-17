package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import entity.HistorialClinico;

public interface HistorialClinicoRepository extends JpaRepository<HistorialClinico, Long> {
    List<HistorialClinico> findByPacienteId(Long pacienteId);
    List<HistorialClinico> findByMedicoId(Long medicoId);
}

