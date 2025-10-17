package repository;

import org.springframework.data.jpa.repository.JpaRepository;

import entity.Cita;

import java.time.LocalDate;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByPacienteId(Long pacienteId);
    List<Cita> findByMedicoId(Long medicoId);
    List<Cita> findByFecha(LocalDate fecha);
}
