package repository;

import org.springframework.data.jpa.repository.JpaRepository;

import entity.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
   
}
