package com.nnk.springboot.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "curvepoint")
public class CurvePoint implements Serializable {

    @Id

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    //@Pattern(regexp = "^([0-9]\\d*|0)$")
    private Integer curveId;

    private Timestamp asOfDate;

    private Double term;

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
