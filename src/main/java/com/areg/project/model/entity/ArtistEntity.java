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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "t_artist", schema = "public")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ArtistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(unique = true, nullable = false, name = "name")
    private String name;
}
