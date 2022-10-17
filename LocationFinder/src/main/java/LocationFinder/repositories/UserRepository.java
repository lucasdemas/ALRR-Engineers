package LocationFinder.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import LocationFinder.models.User;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    @Query(value = "select * from user", nativeQuery = true)
    List<User> findByTemplate(@Param("user_id") Integer user_id,
                              @Param("user_name") String username,
                              @Param("user_email") String email);
}