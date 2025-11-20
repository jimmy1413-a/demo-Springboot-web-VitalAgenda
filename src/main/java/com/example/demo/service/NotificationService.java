package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Cita;
import com.example.demo.entity.Medico;
import com.example.demo.entity.Paciente;
import com.example.demo.entity.Recordatorio;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.CitaRepository;
import com.example.demo.repository.RecordatorioRepository;

@Service
public class NotificationService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private RecordatorioRepository recordatorioRepository;

    /**
     * Crea notificaciones para una cita específica
     */
    public void crearNotificacionesParaCita(Cita cita) {
        if (cita.getPaciente() != null) {
            // Notificación para el paciente
            String mensajePaciente = String.format(
                "Recuerda que tienes una cita programada para hoy %s con el Dr. %s",
                cita.getHora(),
                cita.getMedico() != null ? cita.getMedico().getNombre() : "Médico"
            );

            Recordatorio notificacionPaciente = new Recordatorio();
            notificacionPaciente.setMensaje(mensajePaciente);
            notificacionPaciente.setFechaEnvio(LocalDateTime.of(cita.getFecha(), LocalTime.of(8, 0))); // 8:00 AM
            notificacionPaciente.setEnviado(false);
            notificacionPaciente.setCita(cita);

            recordatorioRepository.save(notificacionPaciente);
        }

        if (cita.getMedico() != null) {
            // Notificación para el médico
            String mensajeMedico = String.format(
                "Recuerda que tienes una cita programada para hoy %s con el paciente %s",
                cita.getHora(),
                cita.getPaciente() != null ? cita.getPaciente().getNombre() : "Paciente"
            );

            Recordatorio notificacionMedico = new Recordatorio();
            notificacionMedico.setMensaje(mensajeMedico);
            notificacionMedico.setFechaEnvio(LocalDateTime.of(cita.getFecha(), LocalTime.of(8, 0))); // 8:00 AM
            notificacionMedico.setEnviado(false);
            notificacionMedico.setCita(cita);

            recordatorioRepository.save(notificacionMedico);
        }
    }

    /**
     * Crea notificaciones para todas las citas de hoy
     */
    public void crearNotificacionesParaHoy() {
        LocalDate hoy = LocalDate.now();

        List<Cita> citasDeHoy = citaRepository.findAll().stream()
            .filter(cita -> cita.getFecha() != null && cita.getFecha().equals(hoy))
            .filter(cita -> cita.getEstado() != null && "CONFIRMADA".equals(cita.getEstado().name()))
            .toList();

        for (Cita cita : citasDeHoy) {
            // Verificar si ya existe una notificación para esta cita
            Recordatorio existente = recordatorioRepository.findByCita(cita);
            if (existente == null) {
                crearNotificacionesParaCita(cita);
            }
        }
    }

    /**
     * Obtiene las notificaciones pendientes para un usuario específico
     */
    public List<Recordatorio> obtenerNotificacionesPendientes(Usuario usuario) {
        LocalDate hoy = LocalDate.now();

        return recordatorioRepository.findAll().stream()
            .filter(recordatorio -> !recordatorio.isEnviado())
            .filter(recordatorio -> {
                if (recordatorio.getCita() == null) return false;

                Cita cita = recordatorio.getCita();
                if (cita.getFecha() == null) return false;

                // Solo mostrar notificaciones de hoy
                return cita.getFecha().equals(hoy) &&
                       ((cita.getPaciente() != null && cita.getPaciente().getId().equals(usuario.getId())) ||
                        (cita.getMedico() != null && cita.getMedico().getId().equals(usuario.getId())));
            })
            .toList();
    }

    /**
     * Marca una notificación como enviada
     */
    public void marcarComoEnviada(Long recordatorioId) {
        recordatorioRepository.findById(recordatorioId).ifPresent(recordatorio -> {
            recordatorio.setEnviado(true);
            recordatorioRepository.save(recordatorio);
        });
    }

    /**
     * Obtiene todas las notificaciones de hoy (para debugging)
     */
    public List<Recordatorio> obtenerNotificacionesDeHoy() {
        LocalDate hoy = LocalDate.now();
        return recordatorioRepository.findAll().stream()
            .filter(recordatorio -> recordatorio.getCita() != null)
            .filter(recordatorio -> recordatorio.getCita().getFecha() != null)
            .filter(recordatorio -> recordatorio.getCita().getFecha().equals(hoy))
            .toList();
    }
}