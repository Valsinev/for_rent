package com.apartment.www.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String color;
    private String name;
    private String Description;
    private int year;
    private int month;
    @ElementCollection
    @CollectionTable(name = "reservation_dates", joinColumns = @JoinColumn(name = "reservation_id"))
    @Column(name = "date")
    private List<LocalDate> dates;

}

