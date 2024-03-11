package com.LUXURYCLIQ.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
//import javax.validation.constraints.Email;
//import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "org.hibernate.typ" + "e.UUIDCharType")
    private UUID uuid;

    @Column(unique=true)
    @NotNull(message ="username is empty")
    private String username;

    @NotNull(message="firstname is empty")
    private String firstName;

    @NotNull(message="lastname is empty")
    private String lastName;

    @NotNull(message="password is empty")
    private String password;

    @Column(unique = true)
    @Email(message="invalid mail")
    private String email;

    @Column(unique = true)
    private String phone;

    private boolean enabled=true;

    private boolean verified=true;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "role_id")
    private Role role;

    private UUID deletedBy;
    private boolean blocked=false;

    private boolean registered;
    private String otp;
    private LocalDateTime otpRequestedTime;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<Address> saveAddAddress=new ArrayList<>();


    @Column (name="forget_password")
    private String forgetpassword;



        }