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
     * Query to find a client in the database by their authentication token (unencrypted).
     * @param clientAuthToken
     * @return
     *      List of clients
     */
    @Query(value = "select * from client where client_auth_token = :clientAuthToken", nativeQuery = true)
    Optional<Client> findByAuthToken(@Param("clientAuthToken") String clientAuthToken);


    /**
     * Query to see if the auth token is already in use by a client in the database
     * @param clientAuth
     * @return
     *      True/False depending if there is a client with the auth token or not
     */
    @Query("select case when count(c)> 0 then true else false end from Client c where lower(c.authToken) like lower(:clientAuth)")
    boolean existsByAuth(@Param("clientAuth") String clientAuth);


}
