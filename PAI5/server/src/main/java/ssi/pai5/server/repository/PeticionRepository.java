package ssi.pai5.server.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ssi.pai5.server.model.Peticion;

public interface PeticionRepository extends JpaRepository<Peticion, Long> {

    @Query("SELECT COUNT(p) FROM Peticion p WHERE p.nonce=:nonce")
    public Integer findNonce(@Param("nonce") String nonce);

    @Query("SELECT COUNT(p) FROM Peticion p WHERE p.timestamp <= :start AND p.timestamp >= :end")
    public Integer countPeticionesEn4Horas(@Param("start") Date start, @Param("end") Date end);
    
}
