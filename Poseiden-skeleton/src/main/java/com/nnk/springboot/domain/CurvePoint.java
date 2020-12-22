package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Entity
@Table(name = "curvepoint")
public class CurvePoint implements Serializable {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Setter
    @Pattern(regexp = "^([0-9]\\d*|0)$")
    private Integer curveId;

    private Timestamp asOfDate;

    @Setter
    @Pattern(regexp = "^0$|^[1-9]\\d*$|^\\.\\d+$|^0\\.\\d*$|^[1-9]\\d*\\.\\d*$")
    private Double term;

    @Setter
    @Pattern(regexp = "^0$|^[1-9]\\d*$|^\\.\\d+$|^0\\.\\d*$|^[1-9]\\d*\\.\\d*$")
    private Double value;

    private Timestamp creationDate;

    public CurvePoint() {
    }

    public CurvePoint(Integer curveId, Double term, Double value) {
        this.curveId = curveId;
        this.term = term;
        this.value = value;
    }
}
