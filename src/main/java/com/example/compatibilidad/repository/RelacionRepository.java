package com.example.compatibilidad.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.compatibilidad.entity.brawlers;
import com.example.compatibilidad.entity.Relacion;

@Repository
public interface RelacionRepository extends JpaRepository<Relacion, Long> {
    Optional<Relacion> findByBrawler1AndBrawler2(brawlers brawler1, brawlers brawler2);
}
