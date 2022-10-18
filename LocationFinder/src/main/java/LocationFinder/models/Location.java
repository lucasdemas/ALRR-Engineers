package LocationFinder.models;

import javax.persistence.*;

@Entity
public class Location {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="location_id")
    private Integer loc_id;

    @Column(name = "location_name")
    private String loc_name;

    @Column(name = "location_area")
    private String loc_area;

    @Column(name = "location_cost")
    private Float loc_cost;

    // Default Constructor
    public Location() {
        this.loc_id=null;
        this.loc_name=null;
        this.loc_area=null;
        this.loc_cost=null;
    }

    public Location(Integer id, String loc, String area, Float cost) {
        this.loc_id=id;
        this.loc_name=loc;
        this.loc_area=area;
        this.loc_cost=cost;
    }

    public Integer getId() {
        return loc_id;
    }

    public void setId(Integer id) {
        this.loc_id=id;
    }

    public String getName() {
        return loc_name;
    }

    public void setName(String name) {
        this.loc_name=name;
    }

    public String getArea() {
        return loc_area;
    }

    public void setArea(String area) {
        this.loc_area=area;
    }

    public Float getCost() {
        return loc_cost;
    }

    public void setCost(Float cost) {
        this.loc_cost=cost;
    }
}
