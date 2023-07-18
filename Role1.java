package com.jwt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Role {

    @PrePersist
    protected void onCreate(){
        this.created_At = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate(){
        this.updated_At = new Date(System.currentTimeMillis());
    }

    @Id
    @SequenceGenerator(name = "role_sequence", sequenceName = "role_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "role_sequence")
    private Long id;
    private String name;
    private String desciption;
    private Date created_At;
    private Date updated_At;

    @ManyToMany(mappedBy = "roles")
    @Fetch(FetchMode.SELECT)
    @JsonIgnore
    private Set<User> user = new HashSet<>();

    System.out.println("Role entity");
System.out.println("Role entity");

    public Role(Long id, String name, String desciption) {
        this.id = id;
        this.name = name;
        this.desciption = desciption;
    }

    public Role(String name) {
        this.name = name;
    }
}
