package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.LoginDto;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService service;



    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

    }

    @Test
    public void test_test() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(model().attributeExists("loginDto"))
                .andExpect(view().name("/login"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Given empty username field, when authenticateUser request, then return error message and login page")
    void givenEmptyUsernameField_whenAuthenticateUser_thenReturnErrorMessageAndLoginPage() throws Exception {
        LoginDto loginDto = new LoginDto("JackLog", "Ursul1pm*");
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/signin")
                .accept(MediaType.ALL)
                .param("username", "")
                .param("password", loginDto.getPassword()))
                .andExpect(model().hasErrors())
                .andExpect(view().name("/login"))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertThat(content).contains("Username is mandatory");
    }

    @Test
    @DisplayName("Given valid credentials and password, when authentication is good, then redirect to bid list page")
    void givenValidCredentialsPassWord_whenAuthenticateUser_thenRedirectToBidListPage() throws Exception {
         UserDto user = new UserDto("JackLog","USER" ,"Ursul1pm*","JackLog");
         LoginDto loginDto = new LoginDto("JackLog", "Ursul1pm*");

        UserDto save= service.save(user);

        mvc.perform(MockMvcRequestBuilders.post("/signin")
                .accept(MediaType.ALL)
                .param("username", loginDto.getUsername())
                .param("password", loginDto.getPassword()))
                .andExpect(model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/bidList/list"));

        service.delete(save.getId());
    }


}
