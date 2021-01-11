package com.nnk.springboot.dto;


import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@JGlobalMap
public class BidListDto {

    private Integer bidListId;
    @NotNull(message = "Account is mandatory")
    private String account;
    @NotNull(message = "Type is mandatory")
    private String type;
    @PositiveOrZero
    @NotNull
    //@Pattern(regexp = "^0$|^[1-9]\\d*$|^\\.\\d+$|^0\\.\\d*$|^[1-9]\\d*\\.\\d*$")
    private Double bidQuantity;
}
