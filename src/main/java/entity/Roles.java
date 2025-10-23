package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public enum Roles {
    PACIENTE,
    MEDICO,
    ADMIN
}


