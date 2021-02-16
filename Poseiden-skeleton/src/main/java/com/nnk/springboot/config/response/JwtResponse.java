package com.nnk.springboot.config.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private Integer id;
    private String username;
    private List<String> roles;

    public JwtResponse(String accessToken, Integer id, String username, List<String> roles) {
        this.token =  accessToken;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}
