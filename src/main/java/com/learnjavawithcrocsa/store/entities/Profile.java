package com.learnjavawithcrocsa.store.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,name = "bio")
    private String bio;
    @Column(nullable = false, name = "phone_number")
    private int phone_number;
    @Column(nullable = false, name = "date_of_birth")
    private Date date_of_birth;
    @Column(nullable = false,name = "loyalty_points")
    private int loyalty_points;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    @MapsId
    @ToString.Exclude
    private User user;
}
