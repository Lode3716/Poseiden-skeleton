package com.nnk.springboot.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.googlecode.jmapper.annotations.JGlobalMap;

@Getter
@Setter
@NoArgsConstructor
@JGlobalMap
public class BidListDto {

    private Integer bidListId;
    private String account;
    private String type;
    private Double bidQuantity;
}
