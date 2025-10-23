package entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pacientes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Paciente extends Usuario {

    private LocalDate fechaNacimiento;
    private String telefono;
    private String direccion;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Cita> citas;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<HistorialClinico> historial;

    public void solicitarCita(Medico medico, LocalDate fecha, LocalTime hora, String motivo) {
        // Aquí podrías implementar la creación de la cita
    }

    public List<Cita> verCitas() {
        return citas;
    }

    public List<HistorialClinico> verHistorial() {
        return historial;
    }

    public void cancelarCita(Long citaId) {
        if (citas != null) {
            citas.removeIf(c -> c.getId().equals(citaId));
        }
    }
}