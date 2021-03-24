package com.nnk.springboot.dto;


import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JGlobalMap
public class RatingDto {

    Integer id;
    @NotBlank(message = "Moodys is mandatory")
    String moodysRating;
    @NotBlank(message = "Sand is mandatory")
    String sandPRating;
    @NotBlank(message = "Fitch is mandatory")
    String fitchRating;
    Integer orderNumber;

    public RatingDto(String moodysRating, String sandPRating, String fitchRating, Integer orderNumber) {
        this.moodysRating = moodysRating;
        this.sandPRating = sandPRating;
        this.fitchRating = fitchRating;
        this.orderNumber = orderNumber;
    }
}
