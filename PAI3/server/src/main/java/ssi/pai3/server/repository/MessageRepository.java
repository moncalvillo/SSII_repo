package ssi.pai3.server.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import ssi.pai3.server.model.Message;

public interface MessageRepository extends JpaRepository<Message,Long> {
    
}
