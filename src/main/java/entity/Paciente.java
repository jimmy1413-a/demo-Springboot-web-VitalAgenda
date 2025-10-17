package entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
    private List<Cita> citas;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
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

