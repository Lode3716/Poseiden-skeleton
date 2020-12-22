package com.nnk.springboot.dto;


import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JGlobalMap
public class BidListDto {

    private Integer bidListId;
    private String account;
    private String type;
    private Double bidQuantity;
}
