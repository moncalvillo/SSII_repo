package ssi.pai2.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ssi.pai2.server.model.Message;


@Repository
public interface ServerRepository extends JpaRepository<Message, Long> {

    @Query("SELECT i FROM Item i WHERE i.path = :path")
    Optional<Message> findItemByPath(@Param("path") String path);

}
