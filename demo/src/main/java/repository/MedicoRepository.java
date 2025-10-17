package repository;

import org.springframework.data.jpa.repository.JpaRepository;

import entity.Medico;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
   
}
