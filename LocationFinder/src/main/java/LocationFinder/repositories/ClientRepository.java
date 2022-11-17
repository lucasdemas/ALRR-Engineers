package LocationFinder.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import LocationFinder.models.Client;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends CrudRepository<Client, Integer> {
    /**
     * Query to find a client in the database by their email.
     * @param clientEmail
     * @return
     *      List of clients
     */
    @Query(value = "select * from client where client_email = :client_email", nativeQuery = true)
    Optional<Client> findByEmail(@Param("client_email") String clientEmail);
}
