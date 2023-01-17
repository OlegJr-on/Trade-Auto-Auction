package com.auction.auto_auction.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@JsonIgnoreProperties({"customer","roles"})
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name",nullable = false)
    private String firstName;

    @Column(name = "last_name",nullable = false)
    private String lastName;

    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDay;

    @Column(name = "phone_number",nullable = false)
    private String phoneNumber;

    @Column(name = "location",nullable = false)
    private String location;

    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @Column(name = "password",nullable = false,unique = true)
    private String password;

    @ManyToMany(mappedBy = "users")
    private List<Role> roles = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private Customer customer;

    public void setRoles(List<Role> roles){
        this.roles = roles;
        roles.forEach(role -> role.getUsers().add(this));
    }
}
