package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "citas")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;
    private LocalTime hora;

    @Enumerated(EnumType.STRING)
    private Estados estado;

    private String motivo;

    @ManyToOne
    @JoinColumn(name = "paciente_id", referencedColumnName = "id")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "medico_id", referencedColumnName = "id")
    private Medico medico;

    @OneToOne(mappedBy = "cita", cascade = CascadeType.ALL)
    private Recordatorio recordatorio;

    public void confirmar() { this.estado = Estados.CONFIRMADA; }

    public void cancelar() { this.estado = Estados.CANCELADA; }

    public boolean esEditable() {
        return this.estado == Estados.PENDIENTE;
    }

    public boolean esEnElFuturo() {
        return this.fecha.isAfter(LocalDate.now()) ||
               (this.fecha.isEqual(LocalDate.now()) && this.hora.isAfter(LocalTime.now()));
    }

    public boolean perteneceA(Paciente paciente) {
        return this.paciente != null && this.paciente.equals(paciente);
    }

    public boolean perteneceA(Medico medico) {
        return this.medico != null && this.medico.equals(medico);
    }
}