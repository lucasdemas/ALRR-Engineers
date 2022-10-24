package LocationFinder.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import LocationFinder.models.Client;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends CrudRepository<Client, Integer> {
    @Query(value = "select * from client", nativeQuery = true)
    List<Client> findByTemplate(@Param("client_id") Integer client_id,
                              @Param("client_name") String client_name,
                              @Param("client_email") String client_email);
}
