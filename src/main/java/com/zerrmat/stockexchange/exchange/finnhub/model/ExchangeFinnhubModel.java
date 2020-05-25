package com.zerrmat.stockexchange.exchange.finnhub.model;

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

@Entity
@Table(name = "exchange")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeFinnhubModel {
    @Id
    @Column(name = "code")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exchange_id_seq")
    @SequenceGenerator(name = "exchange_id_gen", sequenceName = "exchange_id_gen")
    private String code;

    @Column(name = "currency")
    private String currency;

    @Column(name = "name")
    private String name;
}
