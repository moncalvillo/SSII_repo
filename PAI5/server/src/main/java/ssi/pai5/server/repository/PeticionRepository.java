package ssi.pai5.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ssi.pai5.server.model.Peticion;

public interface PeticionRepository extends JpaRepository<Peticion, Long> {
    
}
