package com.example.compatibilidad.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class ImpactoEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImpactoEvento;

    
    
    @ManyToOne
    @JoinColumn(name = "id_tipo_evento", referencedColumnName = "idTipoEvento") // Asegúrate de que la columna en la DB sea correcta
    private TipoEvento tipoEvento;

    @Column(name = "tipo_impacto")
    private String tipoImpacto;

    @Column(name = "descripcion_impacto")
    private String descripcionImpacto;
}
