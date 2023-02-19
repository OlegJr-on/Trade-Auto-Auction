package com.auction.auto_auction.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
@JsonIgnoreProperties({"ordersDetails"})
public class Order {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "bid_id",referencedColumnName = "id")
    private Bid bid;

    @OneToMany(mappedBy = "order",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<OrdersDetails> ordersDetails = new ArrayList<>();

    public Lot getLot(){
        return this.bid.getLot();
    }

    public Customer getCustomer(){
        return this.bid.getCustomer();
    }

    public OrdersDetails getFirstOrderDetail(){
        return this.ordersDetails.get(0);
    }
}
