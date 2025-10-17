package entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "recordatorios")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Recordatorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="mensaje")
    private String mensaje;
    @Column(name ="fecha_envio")
    private LocalDateTime fechaEnvio;
    @Column(name = "enviado")
    private boolean enviado;

    @Column(name ="cita")
    @OneToOne
    @JoinColumn(name = "cita_id", referencedColumnName = "id")
    private Cita cita;

    public void enviar() { }
    public boolean estaPendiente() { return false; }
    public boolean fueEnviado() { return enviado; }


}
