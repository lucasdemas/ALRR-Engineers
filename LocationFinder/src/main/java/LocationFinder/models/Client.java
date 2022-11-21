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
     * Client Authentication token
     */
    @Column(name = "client_auth_token")
    private String authToken;

    /**
     * Client Role (used for authorization)
     */
    @Column(name = "client_role")
    private String role;

    /**
     * The default constructor.
     */
    public Client() {
        this.id = null;
        this.name = null;
        this.email = null;
        this.authToken = null;
        this.role = null;
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
                  final String clientAuthToken) {
        this.id = clientId;
        this.name = clientName;
        this.email = clientEmail;
        this.authToken = clientAuthToken;
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
     * Auth token getter.
     * @return
     *      client auth token
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * auth token setter.
     * @param clientAuthToken
     */
    public void setAuthToken(final String clientAuthToken) {
        this.authToken = clientAuthToken;
    }

    /**
     * Client Role getter
     * @return
     *      client role
     */
    public String getRole() {
        return role;
    }

    /**
     * auth token setter.
     * @param clientRole
     */
    public void setRole(final String clientRole) {
        this.role = clientRole;
    }
}
