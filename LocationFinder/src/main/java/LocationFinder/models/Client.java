package LocationFinder.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Client {
    /**
     * Client Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "client_id")
    private Integer id;

    /**
     * Client Name.
     */
    @Column(name = "client_name")
    private String name;

    /**
     * Client Email.
     */
    @Column(name = "client_email")
    private String email;

    /**
     * Client Password (encrypted)
     */
    @Column(name = "client_password")
    private String password;

    /**
     * The default constructor.
     */
    public Client() {
        this.id = null;
        this.name = null;
        this.email = null;
        this.password = null;
    }
    /**
     * A client object constructor.
     * @param clientId
     * @param clientName
     * @param clientEmail
     */
    public Client(final Integer clientId,
                final String clientName,
                final String clientEmail) {
        this.id = clientId;
        this.name = clientName;
        this.email = clientEmail;
    }

    public Client(final Integer clientId,
                  final String clientName,
                  final String clientEmail,
                  final String clientPassword) {
        this.id = clientId;
        this.name = clientName;
        this.email = clientEmail;
        this.password = clientPassword;
    }

    /**
     * Id getter.
     * @return
     *      client Id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Id setter.
     * @param clientId
     */
    public void setId(final Integer clientId) {
        this.id = clientId;
    }

    /**
     * Name getter.
     * @return
     *      client name
     */
    public String getName() {
        return name;
    }

    /**
     * Name setter.
     * @param clientName
     */
    public void setName(final String clientName) {
        this.name = clientName;
    }

    /**
     * Email getter.
     * @return
     *      client email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Email setter.
     * @param clientEmail
     */
    public void setEmail(final String clientEmail) {
        this.email = clientEmail;
    }

    /**
     * Password getter.
     * @return
     *      client password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Password setter.
     * @param clientPassword
     */
    public void setPassword(final String clientPassword) {
        this.email = clientPassword;
    }
}
