package ssi.pai3.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ssi.pai3.server.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {


    @Query("SELECT u from User u WHERE u.username = :username AND u.password = :password")
    User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);


    Optional<User> findByUsername(String username);

}
