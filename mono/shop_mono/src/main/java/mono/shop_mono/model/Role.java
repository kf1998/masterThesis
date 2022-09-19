package mono.shop_mono.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@AllArgsConstructor
public enum Role implements GrantedAuthority {

    ADMIN("ADMIN"),
    CUSTOMER("CUSTOMER");

    private final String authority;
}