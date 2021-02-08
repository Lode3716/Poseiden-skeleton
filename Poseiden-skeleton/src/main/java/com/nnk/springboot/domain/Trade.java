package com.nnk.springboot.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.sql.Timestamp;


@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"tradeId"})
@Entity
@Table(name = "trade")
public class Trade implements Serializable {

    @Id
    @Column(name = "tradeId")
    @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer tradeId;

    @Setter
    @NotNull(message = "Account is mandatory")
    private String account;

    @Setter
    @NotNull(message = "Type is mandatory")
    private String type;

    @Setter
    @PositiveOrZero
    @NotNull
    private Double buyQuantity;

    private Double sellQuantity;

    private Double buyPrice;

    private Double sellPrice;

    private String benchmark;

    private Timestamp tradeDate;

    private String security;

    private String status;

    private String trader;

    private String book;

    private String creationName;

    private Timestamp creationDate;

    private String revisionName;

    private Timestamp revisionDate;

    private String dealName;

    private String dealType;

    private String sourceListId;

    private String side;


    public Trade(@NotNull(message = "Account is mandatory") String account, @NotNull(message = "Type is mandatory") String type) {
        this.account = account;
        this.type = type;
    }

    public Trade(@NotNull(message = "Account is mandatory") String account, @NotNull(message = "Type is mandatory") String type, @PositiveOrZero @NotNull Double buyQuantity) {
        this.account = account;
        this.type = type;
        this.buyQuantity = buyQuantity;
    }

}
