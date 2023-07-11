/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.entities;

import com.areg.project.QuizRecords;
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
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class User {

    @Id
    //  FIXME !! Understand & choose the strategy
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String userName;

    //  FIXME !!
    @Column
    private QuizRecords record;


    //  private String country;

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }
}
