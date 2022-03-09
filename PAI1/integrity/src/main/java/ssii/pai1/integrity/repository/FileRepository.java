package ssii.pai1.integrity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ssii.pai1.integrity.model.IntegrityFile;

@Repository
public interface FileRepository extends JpaRepository<IntegrityFile, Long> {

    @Query("SELECT f.path FROM IntegrityFile f")
    List<String> findPaths();
}
