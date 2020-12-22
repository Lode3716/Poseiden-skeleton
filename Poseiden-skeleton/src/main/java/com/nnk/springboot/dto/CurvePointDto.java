package com.nnk.springboot.dto;


import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JGlobalMap
public class CurvePointDto {

    private Integer id;
    private Integer curveId;
    private Double term;
    private Double value;
}
