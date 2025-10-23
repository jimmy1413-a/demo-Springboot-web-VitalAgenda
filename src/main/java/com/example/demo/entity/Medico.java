// ...existing code...
package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "medicos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Medico extends Usuario {

    @Column(name = "especialidad", nullable = false, length = 100)
    private String especialidad;

    @Column(name = "consultorio", nullable = false, length = 50)
    private String consultorio;

    // Un médico tiene muchos horarios
    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Horario> horarios = new ArrayList<>();

    // Un médico tiene muchas citas
    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Cita> citas = new ArrayList<>();

    // Un médico tiene muchos historiales clínicos
    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<HistorialClinico> historiales = new ArrayList<>();

    // ==== MÉTODOS DE LÓGICA ====

    public void agregarHorario(Horario horario) {
        horarios.add(horario);
        horario.setMedico(this); // asegura la relación bidireccional
    }

    public void eliminarHorario(Horario horario) {
        horarios.remove(horario);
        if (horario != null) {
            horario.setMedico(null);
        }
    }

    public void verCitasPendientes() {
    }

    public void registrarDiagnostico(Paciente paciente, String diagnostico, String tratamiento, String notas) {
        // Aquí podrías crear y guardar un nuevo HistorialClinico
        // Ejemplo:
        /*
        HistorialClinico historial = new HistorialClinico();
        historial.setPaciente(paciente);
        historial.setMedico(this);
        historial.setDiagnostico(diagnostico);
        historial.setTratamiento(tratamiento);
        historial.setNotas(notas);
        historiales.add(historial);
        */
    }
}
// ...existing code...