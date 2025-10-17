package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import entity.HistorialClinico;

@Repository
public interface  HistorialClinicoRepository extends JpaRepository<HistorialClinico, Long> {
    
}