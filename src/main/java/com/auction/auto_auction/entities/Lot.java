package com.auction.auto_auction.entities;

import com.auction.auto_auction.entities.enums.LotStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lots")
public class Lot {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "lot_status",nullable = false)
    private LotStatus lotStatus;

    @Column(name = "launch_price",nullable = false)
    private BigDecimal launchPrice;

    @Column(name = "min_rate",nullable = false)
    private BigDecimal minRate;

    @Column(name = "start_trading",nullable = false)
    private Date startTrading;

    @Column(name = "end_trading",nullable = false)
    private Date endTrading;
}
