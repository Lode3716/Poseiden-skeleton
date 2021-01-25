package com.nnk.springboot.dto;


import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JGlobalMap
public class BidListDto {

    private Integer bidListId;
    @NotBlank(message = "Account is mandatory")
    private String account;
    @NotBlank(message = "Type is mandatory")
    private String type;
    @PositiveOrZero(message = "Not negative")
    @NotNull(message = "Not null")
    private Double bidQuantity;

    public BidListDto(@NotBlank(message = "Account is mandatory") String account, @NotBlank(message = "Type is mandatory") String type, @PositiveOrZero(message = "Not negative") @NotNull(message = "Not null") Double bidQuantity) {
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
    }
}
