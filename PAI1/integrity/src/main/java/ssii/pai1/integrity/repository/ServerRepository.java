package ssii.pai1.integrity.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ssii.pai1.integrity.model.Item;

@Repository
public interface ServerRepository extends CrudRepository<Item,String> {
    
}
