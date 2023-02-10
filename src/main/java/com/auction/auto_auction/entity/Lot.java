package com.auction.auto_auction.entity;

import com.auction.auto_auction.entity.enums.LotStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lots")
@JsonIgnoreProperties({"salesInfo","bids"})
public class Lot {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "lot_status",nullable = false)
    @Enumerated(EnumType.STRING)
    private LotStatus lotStatus;

    @Column(name = "launch_price",nullable = false)
    private BigDecimal launchPrice;

    @Column(name = "min_rate",nullable = false)
    private BigDecimal minRate;

    @Column(name = "start_trading",nullable = false)
    private LocalDateTime startTrading;

    @Column(name = "end_trading",nullable = false)
    private LocalDateTime endTrading;

    @OneToOne
    @JoinColumn(name = "car_id",referencedColumnName = "id")
    private Car car;

    @ManyToMany(mappedBy = "lots")
    private List<SalesDepartment> salesInfo = new ArrayList<>();

    @OneToMany(mappedBy = "lot",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Bid> bids = new ArrayList<>();

    public void addSale(SalesDepartment sale){
        this.salesInfo.add(sale);
    }

    public void removeSale(SalesDepartment sale){
        this.salesInfo.remove(sale);
    }

    public void addBid(Bid bid){
        this.getBids().add(bid);
    }

    public void removeBid(Bid bid){
        this.getBids().remove(bid);
    }
}
