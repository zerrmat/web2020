package com.zerrmat.stockexchange.stock.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "stock")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockModel {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_id_seq")
    @SequenceGenerator(name = "stock_id_gen", sequenceName = "stock_id_gen")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "currency")
    private String currency;
}
