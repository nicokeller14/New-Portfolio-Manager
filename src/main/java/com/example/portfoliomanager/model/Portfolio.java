package com.example.portfoliomanager.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor

@Entity(name = "Portfolio")
@Table(name = "portfolio")
public class Portfolio {

    @Id
    private long portfolioId;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date openingDate;

    @Column
    private String portfolioName;

    @ManyToOne
    @JoinColumn(name = "investor_id", referencedColumnName = "investorId")
    private Investor investor;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL)
    private Set<StockTrade> stockTransactions;

    public Portfolio(long portfolioId, Date openingDate, String portfolioName, Investor investor) {
        this.portfolioId = portfolioId;
        this.openingDate = openingDate;
        this.portfolioName = portfolioName;
        this.investor = investor;
        this.stockTransactions = new HashSet<>();
    }

}
