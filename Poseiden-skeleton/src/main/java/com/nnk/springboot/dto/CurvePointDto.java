package com.nnk.springboot.dto;


import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JGlobalMap
public class CurvePointDto {

    private Integer id;
    @NotNull(message = "Must not be null")
    private Integer curveId;
    private Double term;
    private Double value;

    public CurvePointDto(@NotNull(message = "Must not be null") Integer curveId, Double term, Double value) {
        this.curveId = curveId;
        this.term = term;
        this.value = value;
    }
}
