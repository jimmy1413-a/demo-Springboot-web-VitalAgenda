package entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pacientes")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Paciente extends Usuario {
    @Column(name ="fecha_nacimiento")
    private Date fechaNacimiento;
    @Column(name="telefono")
    private String telefono;
    @Column(name ="direccion")
    private String direccion;

    @OneToMany(mappedBy = "pacientes", cascade = CascadeType.ALL,fetch=FetchType.LAZY)
    @JsonManagedReference
    private List<Cita> citas;

    @OneToMany(mappedBy = "pacientes", cascade = CascadeType.ALL,fetch=FetchType.LAZY)
    @JsonManagedReference
    private List<HistorialClinico> historial;

    public void solicitarCita(Medico medico, LocalDate fecha, java.time.LocalTime hora, String motivo) {
        // lógica para crear cita
    }

    public List<Cita> verCitas() {
        return citas;
    }

    public List<HistorialClinico> verHistorial() {
        return historial;
    }

    public void cancelarCita(Long citaId) {
        // lógica para cancelar cita
    }
}

