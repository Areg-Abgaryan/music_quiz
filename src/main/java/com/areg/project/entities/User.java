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

//  FIXME !! Add personal records for each mode, country, maybe auth info (keys)

@Table(name = "users", schema = "public")
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class User {

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

    //  FIXME !!
    //  private QuizRecords record;
    //  private String country;

    public User(String username, String email, String passwordSalt, String password) {
        this.username = username;
        this.email = email;
        this.passwordSalt = passwordSalt;
        this.password = password;
    }
}
