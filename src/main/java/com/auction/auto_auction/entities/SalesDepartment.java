package com.auction.auto_auction.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sales_department")
public class SalesDepartment {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "location",nullable = false)
    private String location;

    @Column(name = "sales_name",nullable = false)
    private String salesName;

    @Column(name = "sales_date",nullable = false)
    private Date salesDate;

    @Column(name = "time_left",nullable = false)
    private Date timeLeft;
}
