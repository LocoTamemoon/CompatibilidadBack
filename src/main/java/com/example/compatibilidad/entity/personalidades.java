package com.example.compatibilidad.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class personalidades {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPersonalidad;  // Esta propiedad debe coincidir con la columna `id_personalidad`

    private String tipoPersonalidad;
    private String descripcion;
}
