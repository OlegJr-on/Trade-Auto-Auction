package com.auction.auto_auction.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
@JsonIgnoreProperties({"bankAccount","bids"})
public class Customer {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "discount")
    private int discount;

    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    @OneToOne(mappedBy = "customer")
    private BankAccount bankAccount;

    @OneToMany(mappedBy = "customer",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Bid> bids = new ArrayList<>();

    public BigDecimal getBankBalance(){
        return this.bankAccount.getBalance();
    }

    public void addBid(Bid bid){
        this.getBids().add(bid);
    }

    public void removeBid(Bid bid){
        this.getBids().remove(bid);
    }
}
