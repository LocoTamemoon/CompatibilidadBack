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
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEvento;

    @ManyToOne
    @JoinColumn(name = "id_relacion")
    private Relacion relacion;

    @ManyToOne
    @JoinColumn(name = "id_tipo_evento")
    private TipoEvento tipoEvento;

    private String descripcionEvento;

    private java.time.LocalDateTime fechaEvento = java.time.LocalDateTime.now();
    

    private String tipoImpacto; // Tipo de impacto (Positivo/Negativo)
    private String descripcionImpacto;
    
    @Column(name = "uid")
    private String uid;

}
