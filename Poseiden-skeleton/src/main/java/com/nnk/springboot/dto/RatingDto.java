package com.nnk.springboot.dto;


import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JGlobalMap
public class RatingDto {

    Integer id;
    String moodysRating;
    String sandPRating;
    String fitchRating;
    Integer orderNumber;
}
