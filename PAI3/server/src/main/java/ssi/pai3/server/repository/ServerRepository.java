package ssi.pai3.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ssi.pai3.server.model.Message;

@Repository
public interface ServerRepository extends JpaRepository<Message, Long> {

}
