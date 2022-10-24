package LocationFinder.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import LocationFinder.models.Client;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends CrudRepository<Client, Integer> {
    /**
     * Query to find a client in the database.
     * @param clientId
     * @param clientName
     * @param clientEmail
     * @return
     *      List of clients
     */
    @Query(value = "select * from client", nativeQuery = true)
    List<Client> findByTemplate(@Param("client_id") Integer clientId,
                              @Param("client_name") String clientName,
                              @Param("client_email") String clientEmail);
}
