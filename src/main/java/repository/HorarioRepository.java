    
package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import entity.Horario;



@Repository
public interface  HorarioRepository extends JpaRepository<Horario, Long> {
    
}