package repository;

import org.springframework.data.jpa.repository.JpaRepository;

import entity.Horario;

public interface HorarioRepository extends JpaRepository<Horario, Long> {
}

