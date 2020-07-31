package com.zerrmat.stockexchange.cachecontrol.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cachecontrol")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CacheControlModel {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cachecontrol_id_gen")
    @SequenceGenerator(name = "cachecontrol_id_gen", sequenceName = "cachecontrol_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "endpoint_name")
    private String endpointName;

    @Column(name = "last_access")
    private LocalDateTime lastAccess;
}
