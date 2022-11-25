package LocationFinder.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "location_data")
public class Location {
    /**
     * Location id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Integer locId;

    /**
     * Client Id of a location.
     */
    @Column(name = "client_id")
    private Integer clientId;

    /**
     * Location name.
     */
    @Column(name = "location_name")
    private String locName;

    /**
     * Location area.
     */
    @Column(name = "location_area")
    private String locArea;

    /**
     * Location cost.
     */
    @Column(name = "location_cost")
    private Double locCost;

    /**
     * Location claim status.
     */
    @Column (name = "claimed")
    private Boolean claim;

    /**
     * Default constructor.
     */
    public Location() {
        this.locId = null;
        this.clientId = null;
        this.locName = null;
        this.locArea = null;
        this.locCost = null;
        this.claim = false;
    }

    /**
     * Location constructor.
     * @param loc
     * @param area
     * @param cost
     * @param clientId
     */
    public Location(final String loc, final String area, final Double cost, final Integer clientId) {
        this.locName = loc;
        this.locArea = area;
        this.locCost = cost;
        this.clientId = clientId;
        this.claim = false;
    }

    /**
     * Id getter.
     * @return
     *      location id
     */
    public Integer getId() {
        return locId;
    }

    /**
     * Id setter.
     * @param id
     */
    public void setId(final Integer id) {
        this.locId = id;
    }

    /**
     * Client Id getter.
     * @return
     *      clientId
     */
    public Integer getClientId() {
        return clientId;
    }

    /**
     * Client Id setter.
     * @param clientId
     */
    public void setClientId(final Integer clientId) {
        this.clientId = clientId;
    }

    /**
     * Name getter.
     * @return
     *      location name
     */
    public String getName() {
        return locName;
    }

    /**
     * Name setter.
     * @param name
     */
    public void setName(final String name) {
        this.locName = name;
    }

    /**
     * Area getter.
     * @return
     *      location area
     */
    public String getArea() {
        return locArea;
    }

    /**
     * Area setter.
     * @param area
     */
    public void setArea(final String area) {
        this.locArea = area;
    }

    /**
     * Cost getter.
     * @return
     *      location cost
     */
    public Double getCost() {
        return locCost;
    }

    /**
     * Cost setter.
     * @param cost
     */
    public void setCost(final Double cost) {
        this.locCost = cost;
    }

    /**
     * Claim status getter.
     * @return
     *      location claim status
     */
    public Boolean getClaim() {
        return claim;
    }

    /**
     * Location claimed status setter.
     * @param cl
     */
    public void setClaim(final Boolean cl) {
        this.claim = cl;
    }
}
