package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name ="estados")
public enum  Estados {
    PENDIENTE,
    CONFIRMADA,
    CANCELADA

}
