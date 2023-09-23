/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//  FIXME !! Add personal records for each mode, maybe auth info (keys)
@Table(name = "users", schema = "public")
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class User {

    /*  FIXME !! Consider adding these fields
    - quizRecords - records for each user on each game mode
    - country - country of a user
    - lockoutEnabled - true or false values and indicates if the user locked their account by accessing too many times with wrong credentials.
    - accountAccessFailCount - this field is containing the number of times when the user tried to access their account with wrong credentials.
            Usually, I'm locking the account after 3 false accesses.
    - lockoutTime - this field is containing the date and time when the account has been locked out.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(unique = true, nullable = false, name = "email")
    private String email;

    @Column(nullable = false, name = "password_salt")
    private String passwordSalt;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(unique = true, nullable = false, name = "username")
    private String username;

    @Column(nullable = false, name = "registration_date")
    private long registrationDate;


    public User(String username, String email, long registrationDate, String passwordSalt, String password) {
        this.username = username;
        this.email = email;
        this.registrationDate = registrationDate;
        this.passwordSalt = passwordSalt;
        this.password = password;
    }
}
