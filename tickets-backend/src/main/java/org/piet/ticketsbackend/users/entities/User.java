package org.piet.ticketsbackend.users.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "app_users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    boolean passwordNeedsChanging = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAsAuthorities();
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public boolean equalsUser(User another){
        return id.equals(another.id);
    }

    public boolean hasAccessAtLeast(Role other){
        return role.hasAccessAtLeast(other);
    }
}
