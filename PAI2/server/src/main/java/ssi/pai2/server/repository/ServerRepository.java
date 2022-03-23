package ssi.pai2.server.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ssi.pai2.server.model.Message;


@Repository
public interface ServerRepository extends JpaRepository<Message, Long> {

    @Query("SELECT count(*) FROM Message m WHERE m.nonce =?1")
    Integer findNonce(String nonce);

    
}
