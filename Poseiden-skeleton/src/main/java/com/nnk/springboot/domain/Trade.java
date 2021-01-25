package com.nnk.springboot.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.sql.Timestamp;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = {"tradeId"})
@Entity
@Table(name = "trade")
public class Trade implements Serializable {

    @Id
    @Setter
    @Column(name = "tradeId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer tradeId;

    @Setter
    @NotNull(message = "Account is mandatory")
    String account;

    @Setter
    @NotNull(message = "Type is mandatory")
    String type;

    @Setter
    @PositiveOrZero
    @NotNull
    Double buyQuantity;

    Double sellQuantity;

    Double buyPrice;

    Double sellPrice;

    String benchmark;

    Timestamp tradeDate;

    String security;

    String status;

    String trader;

    String book;

    String creationName;

    Timestamp creationDate;

    String revisionName;

    Timestamp revisionDate;

    String dealName;

    String dealType;

    String sourceListId;

    String side;


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
