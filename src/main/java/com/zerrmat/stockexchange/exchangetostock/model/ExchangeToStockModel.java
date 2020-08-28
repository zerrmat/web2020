package com.zerrmat.stockexchange.exchangetostock.model;

import com.zerrmat.stockexchange.exchange.model.ExchangeModel;
import com.zerrmat.stockexchange.stock.model.StockModel;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "exchangetostock")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeToStockModel {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exchangetostock_id_seq")
    @SequenceGenerator(name = "exchangetostock_id_gen", sequenceName = "exchangetostock_id_gen")
    private Long id;

    @ManyToOne
    @MapsId("exchange_id")
    private ExchangeModel exchange;

    @ManyToOne
    @MapsId("stock_id")
    private StockModel stock;
}
