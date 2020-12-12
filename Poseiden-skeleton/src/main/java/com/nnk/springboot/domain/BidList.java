package com.nnk.springboot.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;

@Getter
@Builder
@Entity
@Table(name = "bidlist")
public class BidList {

    @Id
    @Column(name = "bidListId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer bidListId;

    @NotNull(message = "Account is mandatory")
    private String account;

    @NotNull(message = "Type is mandatory")
    private String type;

    @Pattern(regexp = "^0$|^[1-9]\\d*$|^\\.\\d+$|^0\\.\\d*$|^[1-9]\\d*\\.\\d*$")
    private Double bidQuantity;

    @Pattern(regexp = "^0$|^[1-9]\\d*$|^\\.\\d+$|^0\\.\\d*$|^[1-9]\\d*\\.\\d*$")
    private Double askQuantity;

    @Pattern(regexp = "^0$|^[1-9]\\d*$|^\\.\\d+$|^0\\.\\d*$|^[1-9]\\d*\\.\\d*$")
    private Double bid;

    @Pattern(regexp = "^0$|^[1-9]\\d*$|^\\.\\d+$|^0\\.\\d*$|^[1-9]\\d*\\.\\d*$")
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

    public BidList() {
    }

    public BidList(@NotNull(message = "Account is mandatory") String account, @NotNull(message = "Type is mandatory") String type, Double bidQuantity) {
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
    }
}
