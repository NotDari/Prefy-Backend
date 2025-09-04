package com.daribear.PrefyBackend.Security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Enum containing the available user roles and a method to get the granted permissions for each role.
 */
public enum ApplicationUserRole {

    User(),
    Admin();


    /**
     * Get the authorities/permissions granted for the role.
     *
     * @return Set of simplegrantedauthorities (the list of authorities that the user has with their role)
     */
    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> permissions = new HashSet<>();
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
