package com.nnk.springboot.dto;

import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@JGlobalMap
public class RuleNameDto {

    Integer id;
    @NotBlank(message = "Name is mandatory")
    String name;
    @NotBlank(message = "Description is mandatory")
    String description;
    String json;
    String template;
    String sqlStr;
    String sqlPart;

    public RuleNameDto(@NotBlank(message = "Name is mandatory") String name, @NotBlank(message = "Description is mandatory") String description, String json, String template, String sqlStr, String sqlPart) {
        this.name = name;
        this.description = description;
        this.json = json;
        this.template = template;
        this.sqlStr = sqlStr;
        this.sqlPart = sqlPart;
    }
}
