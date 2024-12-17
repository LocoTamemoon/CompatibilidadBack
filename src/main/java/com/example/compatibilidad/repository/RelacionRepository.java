package com.example.compatibilidad.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.compatibilidad.entity.Relacion;
import com.example.compatibilidad.entity.brawlers;

@Repository
public interface RelacionRepository extends JpaRepository<Relacion, Long> {
	Optional<Relacion> findByBrawler1AndBrawler2AndUid(brawlers brawler1, brawlers brawler2, String uid);
	
    List<Relacion> findByUid(String uid);

}
