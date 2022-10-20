package LocationFinder.models;

import javax.persistence.*;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="client_id")
    private Integer id;

    @Column(name="client_name")
    private String name;

    @Column(name="client_email")
    private String email;

    public Client() {
        this.id=null;
        this.name=null;
        this.email=null;
    }
    public Client(Integer client_id,
                String client_name,
                String client_email) {
        this.id=client_id;
        this.name=client_name;
        this.email=client_email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
