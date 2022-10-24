package LocationFinder.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import LocationFinder.models.Location;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
