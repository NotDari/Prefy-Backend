package com.daribear.PrefyBackend.Security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public enum ApplicationUserRole {
    /**
    USER(Sets.newHashSet(ApplicationUserPermissions.Large_Data_Read)),
    Admin(Sets.newHashSet(ApplicationUserPermissions.Large_Data_Read, ApplicationUserPermissions.Suggestion_READ, ApplicationUserPermissions.Report_Read));
     */
    User(),
    Admin,
    Owner;

    /**private final Set<ApplicationUserPermissions> permissions;

    ApplicationUserRole(Set<ApplicationUserPermissions> permissions) {
        this.permissions = permissions;
    }

    //public Set<ApplicationUserPermissions> getPermissions() {
        return permissions;
    }


    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){

        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("Role_" + this.name()));
        return permissions;
    }
     */

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> permissions = new HashSet<>();
        permissions.add(new SimpleGrantedAuthority("Role_" + this.name()));
        return permissions;
    }
}
