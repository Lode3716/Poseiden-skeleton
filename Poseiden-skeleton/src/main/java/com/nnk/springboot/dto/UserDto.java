package com.nnk.springboot.dto;

import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JGlobalMap
public class UserDto {

    private Integer id;

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    private String role;

    @NotBlank
    @Pattern(regexp ="^(?=.*[A-Z])(?=.*[!@#$&_*])(?=.*[0-9]).{8,15}$" , message = "The password must contain at least 8 characters, one uppercase letter, one number and one symbol")
    private String password;

    @NotBlank(message = "FullName is mandatory")
    String fullname;

    public UserDto(@NotBlank @Size(min = 3, max = 20) String username, @NotBlank String role, @NotBlank @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$&_*])(?=.*[0-9]).{8,15}$", message = "The password must contain at least 8 characters, one uppercase letter, one number and one symbol") String password, @NotBlank(message = "FullName is mandatory") String fullname) {
        this.username = username;
        this.role = role;
        this.password = password;
        this.fullname = fullname;
    }
}
