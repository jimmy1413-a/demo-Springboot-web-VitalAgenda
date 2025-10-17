package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import entity.Cita;

@Repository
public interface  CitaRepository extends JpaRepository<Cita, Long> {
    
}