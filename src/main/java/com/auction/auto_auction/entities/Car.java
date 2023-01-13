package com.auction.auto_auction.entities;

import com.auction.auto_auction.entities.enums.AutoState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cars")
@JsonIgnoreProperties({"lot","photos"})
public class Car {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "mark",nullable = false)
    private String mark;

    @Column(name = "model",nullable = false)
    private String model;

    @Column(name = "first_registry_date",nullable = false)
    private Date registryDate;

    @Column(name = "run",nullable = false)
    private int run;

    @Column(name = "weight_kg")
    private int weight;

    @Column(name = "damage",nullable = false)
    private String damage;

    @Column(name = "state",nullable = false)
    @Enumerated(EnumType.STRING)
    private AutoState state;

    @Column(name = "nominal_value",nullable = false)
    private BigDecimal nominalValue;

    @Column(name = "oriented_price")
    private BigDecimal orientedPrice;

    @OneToMany(mappedBy = "car",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<AutoPhoto> photos = new ArrayList<>();

    @OneToOne(mappedBy = "car")
    private Lot lot;

    public void setLot(Lot lot){
        this.lot = lot;
        lot.setCar(this);
    }
}
