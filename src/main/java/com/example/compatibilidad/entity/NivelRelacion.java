package com.example.compatibilidad.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NivelRelacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNivelRelacion;

    private String nivelRelacion;
}
