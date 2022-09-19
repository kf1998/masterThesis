package mono.shop_mono.model;

import lombok.Data;
import mono.shop_mono.dto.request.UserRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {

    private boolean enabled = true;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin = false;

    public User() {}

    public User(String username, String password, String firstname, String lastname, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.isAdmin = isAdmin;
    }

    public User(String username, UserRequest userRequest) {
        this.username = username;
        this.password = userRequest.getPassword();
        this.firstname = userRequest.getFirstName();
        this.lastname = userRequest.getLastName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(isAdmin ? Role.ADMIN : Role.CUSTOMER);
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}