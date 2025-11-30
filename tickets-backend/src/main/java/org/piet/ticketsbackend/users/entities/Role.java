package org.piet.ticketsbackend.users.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public enum Role {
    PASSENGER(0),
    ADMIN(1);
    final int accessLevel;

    Role(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public boolean hasAccessAtLeast(Role other){
        return this.accessLevel >= other.accessLevel;
    }

    public List<GrantedAuthority> getAsAuthorities(){
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role r : Role.values()){
            if (r.accessLevel <= this.accessLevel){
                authorities.add(new SimpleGrantedAuthority("ROLE_" + r.name()));
            }
        }
        return authorities;
    }
}
