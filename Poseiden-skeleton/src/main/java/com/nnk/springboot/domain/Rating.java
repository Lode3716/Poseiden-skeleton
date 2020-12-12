package com.nnk.springboot.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;

@FieldDefaults(level=AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "rating")
public class Rating {

    @Id
    @NotNull
    @GeneratedValue(strategy= GenerationType.AUTO)
    Integer id ;

    String moodysRating;

    String sandPRating;

    String fitchRating;

    Integer orderNumber;

}
