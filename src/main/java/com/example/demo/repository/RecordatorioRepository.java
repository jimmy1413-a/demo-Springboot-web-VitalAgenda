package com.example.demo.repository;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Cita;
import com.example.demo.entity.Recordatorio;

@Repository
public interface RecordatorioRepository extends JpaRepository<Recordatorio, Long> {
        // Buscar recordatorios por cita
    Recordatorio findByCita(Cita cita);

    // Buscar recordatorios pendientes
    List<Recordatorio> findByEnviadoFalse();

    // Buscar recordatorios enviados
    List<Recordatorio> findByEnviadoTrue();

    // Buscar recordatorios por fecha de envío
    List<Recordatorio> findByFechaEnvioBefore(LocalDateTime fecha);

    // Buscar recordatorios programados para una fecha específica
    List<Recordatorio> findByFechaEnvio(LocalDateTime fecha);
}

