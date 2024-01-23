/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

//  FIXME !! Add personal records for each mode, maybe auth info (keys)
@Table(name = "t_user", schema = "public")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    /*  FIXME !! Consider adding these fields
    - quizRecords - records for each user on each game mode
    - country - country of a user
    - lockoutEnabled - true/false values, indicates if the user locked their account by accessing too many times with wrong credentials.
    - accountAccessFailCount - containing the number of times when the user tried to access their account with wrong credentials.
            Usually, I'm locking the account after 3 false accesses.
    - lockoutTime - this field is containing the date and time when the account has been locked out.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "external_id")
    private UUID externalId;

    @Column(unique = true, nullable = false, name = "username")
    private String username;

    @Column(unique = true, nullable = false, name = "email")
    private String email;

    @Column(nullable = false, name = "salt")
    private String salt;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, name = "registration_date")
    private long registrationDate;


    public UserEntity(String username, String email, String salt, String encryptedPassword) {
        this.username = username;
        this.email = email;
        this.salt = salt;
        this.password = encryptedPassword;
    }
}
