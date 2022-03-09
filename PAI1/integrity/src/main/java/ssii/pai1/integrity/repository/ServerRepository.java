package ssii.pai1.integrity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ssii.pai1.integrity.model.Item;

@Repository
public interface ServerRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE i.path = :path")
    Optional<Item> findItemByPath(@Param("path") String path);

}
