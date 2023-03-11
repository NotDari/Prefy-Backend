package com.daribear.PrefyBackend.Authentication;


import com.daribear.PrefyBackend.Security.ApplicationUserRole;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity

public class Authentication implements UserDetails {
    @Id
    @SequenceGenerator(
            name = "Authentication_Sequence",
            sequenceName = "Authentication_Sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "Authentication_Sequence"
    )
    private Long id;
    private String email;
    private String password;
    private Boolean locked;
    private Boolean enabled;
    private HashSet<SimpleGrantedAuthority> grantedAuthorities;


    public Authentication(String email, String password, Set<SimpleGrantedAuthority> grantedAuthorities) {
        this.email = email;
        this.password = password;
        this.grantedAuthorities =  new HashSet<>(grantedAuthorities);
    }




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
