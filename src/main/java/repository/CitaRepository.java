package repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import entity.Cita;
import entity.Estados;
import entity.Medico;
import entity.Paciente;

@Repository
public interface  CitaRepository extends JpaRepository<Cita, Long> {
    // Buscar citas por paciente
    List<Cita> findByPaciente(Paciente paciente);

    // Buscar citas por médico
    List<Cita> findByMedico(Medico medico);

    // Buscar citas por fecha
    List<Cita> findByFecha(LocalDate fecha);

    // Buscar citas pendientes
    List<Cita> findByEstado(Estados estado);

    // Buscar citas futuras para un paciente
    List<Cita> findByPacienteAndFechaAfter(Paciente paciente, LocalDate fechaActual);

    // Buscar citas por médico y fecha
    List<Cita> findByMedicoAndFecha(Medico medico, LocalDate fecha);
}

