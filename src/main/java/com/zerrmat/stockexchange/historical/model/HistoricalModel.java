package com.zerrmat.stockexchange.historical.model;

import com.zerrmat.stockexchange.exchangetostock.model.ExchangeToStockModel;
import com.zerrmat.stockexchange.stock.model.StockModel;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Table(name = "historical")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalModel {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historical_id_seq")
    @SequenceGenerator(name = "historical_id_gen", sequenceName = "historical_id_gen")
    private Long id;

    @ManyToOne
    @MapsId("ets_id")
    private ExchangeToStockModel ets;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "volume")
    private Long volume;

    @Column(name = "date")
    private ZonedDateTime date;
}
