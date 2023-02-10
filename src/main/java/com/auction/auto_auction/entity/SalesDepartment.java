package com.auction.auto_auction.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private LocalDateTime salesDate;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "lots_sales",
            joinColumns = @JoinColumn(name = "sale_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "lot_id",referencedColumnName = "id")
    )
    private List<Lot> lots = new ArrayList<>();

    public void addLot(Lot lot){
        this.lots.add(lot);
    }

    public void removeLot(Lot lot){
        this.lots.remove(lot);
    }
}
