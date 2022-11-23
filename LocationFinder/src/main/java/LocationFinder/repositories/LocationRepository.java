package LocationFinder.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import LocationFinder.models.Location;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

@Repository
public interface LocationRepository extends CrudRepository<Location, Integer> {
    /**
     * Query to find a location in the database.
     * @param locId
     * @param locName
     * @param locArea
     * @param locCost
     * @param claim
     * @return
     *      List of locations
     */
    @Query(value = "select * from location_data", nativeQuery = true)
    List<Location> findByTemplate(@Param("location_id") Integer locId,
                              @Param("location_name") String locName,
                              @Param("location_area") String locArea,
                              @Param("location_cost") Double locCost,
                              @Param("claimed") Boolean claim);
    /**
     * Query to find a location by area.
     * @param locationArea
     * @return
     *      List of locations by area
     */
    @Query(value =
    "select * from location_data where location_area = :location_area",
     nativeQuery = true)
    List<Location> findByArea(@Param("location_area") String locationArea);

    /**
     * Query to find locations by claimed status.
     * @param claimed
     * @return
     *      List of locations by claimed status
     */
    @Query(value = "select * from location_data where claimed = :claimed",
            nativeQuery = true)
    List<Location> findByClaim(@Param("claimed") Boolean claimed);

    /**
     * Query to delete all the locations of a client that has been deleted
     * @param client_id
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE from location_data where client_id = :client_id",
            nativeQuery = true)
    void deleteClientLocs(@Param("client_id") Integer client_id);

    /**
     * Query to find locations by claimed status.
     * @param client_id
     *      The id of the client getting all their locations
     * @return
     *      List of locations for that client id
     */
    @Query(value = "select * from location_data where client_id = :client_id",
            nativeQuery = true)
    Iterable<Location> getAllByClientId(@Param("client_id") Integer client_id);
}
