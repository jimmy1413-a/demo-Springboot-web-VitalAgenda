    
package com.example.demo.repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Horario;
import com.example.demo.entity.Medico;



@Repository
public interface  HorarioRepository extends JpaRepository<Horario, Long> {
     // Buscar horarios por médico
    List<Horario> findByMedico(Medico medico);

    // Buscar horarios por médico y día de la semana
    List<Horario> findByMedicoAndDiaSemana(Medico medico, DayOfWeek diaSemana);

    // Buscar horarios por médico, día y hora específica
    List<Horario> findByMedicoAndDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(
        Medico medico, DayOfWeek diaSemana, LocalTime horaInicio, LocalTime horaFin
    );

}