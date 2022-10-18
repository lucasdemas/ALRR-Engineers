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
    @Query(value = "select * from location_data", nativeQuery = true)
    List<Location> findByTemplate(@Param("location_id") Integer loc_id,
                              @Param("location_name") String loc_name,
                              @Param("location_area") String loc_area,
                              @Param("location_cost") Double loc_cost,
                              @Param("claimed") Boolean claim);

    // @Query(value = "insert into location_data values(:loc_id, :loc_name, :loc_area, :loc_cost)", nativeQuery = true)
    // void insertLoc(@Param("location_id") Integer loc_id,
    //             @Param("location_name") String loc_name,
    //             @Param("location_area") String loc_area,
    //             @Param("location_cost") Float loc_cost);

    @Query(value = "select * from location_data where location_area = :location_area", nativeQuery = true)
    List<Location> findByArea(@Param("location_area") String location_area);
}