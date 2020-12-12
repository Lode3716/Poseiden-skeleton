package com.nnk.springboot.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "Username is mandatory")
    private String username;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$&_*])(?=.*[0-9]).{8,15}$")
    @NotBlank(message = "Password is mandatory")
    String password;

    @NotBlank(message = "FullName is mandatory")
    String fullname;

    @NotBlank(message = "Role is mandatory")
    String role;


}
