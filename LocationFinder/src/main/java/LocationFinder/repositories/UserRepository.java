package LocationFinder.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import LocationFinder.models.User;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

<<<<<<< HEAD

public interface UserRepository extends CrudRepository<User, Integer> {
    @Query(value = "select * from user")

    List<User> findByTemplate(@Param (name) String name,
                              @Param (email) String email);

=======
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    @Query(value = "select * from user", nativeQuery = true)
    List<User> findByTemplate(@Param("user_id") Integer user_id,
                              @Param("user_name") String username,
                              @Param("user_email") String email);
>>>>>>> cfc71c8a245c38507543615fc2035b09addd136c
}