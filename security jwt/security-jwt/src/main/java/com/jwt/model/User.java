package com.jwt.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
public class User implements UserDetails {

    @PrePersist
    protected void onCreate(){
        this.created_At = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate(){
        this.updated_At = new Date(System.currentTimeMillis());
    }

    public User(String mobile_number, String user_name, String email, String password, Set<Role> roles) {
        this.user_id= email;
        this.user_name = user_name;
        this.email = email;
        this.password = password;
        this.mobile_number = mobile_number;
        this.roles = roles;
    }
    @Id
    private String user_id;
    private String user_name;
    private String email;
    private String password;
    private String mobile_number;

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "User_id"), inverseJoinColumns = @JoinColumn(name = "Role_id"))
    private Set<Role> roles = new HashSet<>();
    private Date created_At;
    private Date updated_At;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.stream().forEach(i->authorities.add(new SimpleGrantedAuthority(i.getName())));
        return List.of(new SimpleGrantedAuthority(authorities.toString()));
    }
    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
