package entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "citas")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "hora")
    private LocalTime hora;

    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private Estados estado;

    @Column(name = "motivo")
    private String motivo;

    @Column(name = "paciente")
    @ManyToOne
    @JoinColumn(name = "paciente_id", referencedColumnName = "id")
    private Paciente paciente;

    @Column(name = "medico")
    @ManyToOne
    @JoinColumn(name = "medico_id", referencedColumnName = "id")
    private Medico medico;

    @Column(name = "recordatorio")
    @OneToOne(mappedBy = "Cita", cascade = CascadeType.ALL)
    @JoinColumn(name = "recordatorio_id", referencedColumnName = "id")
    private Recordatorio recordatorio;

    public void confirmar() { }
    public void cancelar() { }
    public boolean esEditable() { return false; }
    public boolean esEnElFuturo() { return false; }
    public boolean perteneceA(Paciente paciente) { return false; }
    public boolean perteneceA(Medico medico) { return false; }


}
