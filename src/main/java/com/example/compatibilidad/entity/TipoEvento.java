package com.example.compatibilidad.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TipoEvento {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long idTipoEvento;

	    @Column(name = "nombre_evento")  // Asegúrate de que el nombre de la columna coincida con el de la base de datos
	    private String nombre;  // Aquí debe estar el campo 'nombre'

	    
	    @Column(name = "descripcion_evento")  // Asegúrate de que el nombre de la columna coincida con el de la base de datos
	    private String descripcion;  
	

          @Column(name = "imagen_evento")  // Asegúrate de que el nombre de la columna coincida con el de la base de datos
	    private String imagenEvento;


    
	
}
