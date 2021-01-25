package com.nnk.springboot.dto;

import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JGlobalMap
public class RuleNameDto {

    Integer id;
    String name;
    String description;
    String json;
    String template;
    String sqlStr;
    String sqlPart;
}
