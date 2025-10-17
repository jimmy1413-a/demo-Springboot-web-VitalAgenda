package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Usuario {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="nombre",nullable=false)
    private String nombre;

    
    @Column(unique = true,name ="email",nullable=false)
    private String email;

    @Column(name ="contraseña",unique=true,nullable=false)
    private String contraseña;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol")
    private Roles rol;

    public boolean autenticar(String email, String contraseña) {
        return this.email.equals(email) && this.contraseña.equals(contraseña);
    }

    public void cambiarContraseña(String nuevaContraseña) {
        this.contraseña = nuevaContraseña;
    }

    public boolean tieneRol(Roles rol) {
        return this.rol == rol;
    }
}


