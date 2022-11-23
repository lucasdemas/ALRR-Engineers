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

    /**
     * Query to see if the email is already in use by a client in the database
     * @param clientEmail
     * @return
     *      True/False depending if there is a client with the email or not
     */
    @Query("select case when count(c)> 0 then true else false end from Client c where lower(c.email) like lower(:clientEmail)")
    boolean existsByEmail(@Param("clientEmail") String clientEmail);

    /**
     * Query to find a client in the database by their authentication token (unencrypted).
     * @param clientAuthToken
     * @return
     *      List of clients
     */
    @Query(value = "select * from client where client_auth_token = :client_auth_token", nativeQuery = true)
    Optional<Client> findByAuthToken(@Param("client_auth_token") String clientAuthToken);
}
