package entity;

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
@Table(name = "medicos")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Medico extends Usuario {

    @Column(name ="especialidad")
    private String especialidad;
    
    @Column(name = "consultorio")
    private String consultorio;

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL)
    private List<Horario> horarios;

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL)
    private List<Cita> citas;

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL)
    private List<HistorialClinico> historiales;

    public void agregarHorario(Horario horario) {
        
    }
    public void eliminarHorario(Horario horario) { 

    }
    public List<Cita> verCitasPendientes() { 
        return null; 
    }
    public void registrarDiagnostico(Paciente paciente, String diagnostico, String tratamiento, String notas) {
        
     }
}

