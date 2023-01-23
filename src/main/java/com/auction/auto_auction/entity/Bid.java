package com.auction.auto_auction.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bids")
@JsonIgnoreProperties({"order"})
public class Bid {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "operation_date",nullable = false)
    private LocalDateTime operationDate;

    @Column(name = "is_active",nullable = false)
    private boolean isActive;

    @Column(name = "bet",nullable = false)
    private BigDecimal bet;

    @ManyToOne
    @JoinColumn(name = "lot_id", referencedColumnName = "id")
    private Lot lot;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @OneToOne(mappedBy = "bid")
    private Order order;
}
