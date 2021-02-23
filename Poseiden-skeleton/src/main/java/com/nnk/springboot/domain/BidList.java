package com.nnk.springboot.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "bidListId")
@Entity
@Table(name = "bidlist")
public class BidList implements Serializable {

    @Id
    @Column(name = "bidListId")
    @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer bidListId;

    @Setter
    @NotNull(message = "Account is mandatory")
    private String account;

    @Setter
    @NotNull(message = "Type is mandatory")
    private String type;

    @Setter
    @PositiveOrZero
    @NotNull
    private Double bidQuantity;

    private Double askQuantity;

    private Double bid;

    private Double ask;

    private String benchmark;

    private Timestamp bidListDate;

    private String commentary;

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

    public BidList(@NotNull(message = "Account is mandatory") String account, @NotNull(message = "Type is mandatory") String type, Double bidQuantity) {
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
    }

    public BidList(Integer bidListId, @NotNull(message = "Account is mandatory") String account, @NotNull(message = "Type is mandatory") String type, @PositiveOrZero @NotNull Double bidQuantity) {
        this.bidListId = bidListId;
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
    }
}
