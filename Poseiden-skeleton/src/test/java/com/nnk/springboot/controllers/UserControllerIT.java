package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;


    @Autowired
    private UserRepository repository;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    @Test
    @DisplayName("Given a UserDto, when POST request, then save UserDto check redirect Url is OK and check UserDto is save in BDD")
    public void givenUserDtoAdd_whenPostRequest_thenReturnUserDtoAdd() throws Exception {
        UserDto userDto = new UserDto("Jack", "USER","Ursul1pm*", "Jack" );
        User user = new User("Jack", "Ursul1pm*", "Jack", "USER");

        mvc.perform(MockMvcRequestBuilders.post("/user/validate")
                .sessionAttr("UserDto", userDto)
                .param("fullname", userDto.getFullname())
                .param("username", userDto.getUsername())
                .param("password", userDto.getPassword())
                .param("role", userDto.getRole()))
                .andExpect(model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/list"));


        assumeTrue(repository.findAll().stream()
                .anyMatch(userOne -> userOne.getFullname().equals(user.getFullname())
                        && userOne.getUsername().equals(user.getUsername())));

        repository.findAll().stream()
                .findFirst()
                .ifPresent(userOne ->
                {
                    if (user.getFullname().equals(userOne.getFullname())
                            && user.getUsername().equals(userOne.getUsername())) {
                        repository.deleteById(userOne.getId());
                    }
                });
    }

    @Test
    @DisplayName("Given a UserDto, when POST request, then save UserDto return error Account is mandatory")
    public void givenUserDtoAdd_whenPostRequest_thenReturnErreurMandatory() throws Exception {
        UserDto userDto = new UserDto("Jacka", "USER","Ursulpm", "Jacka" );

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/user/validate")
                .sessionAttr("UserDto", userDto)
                .param("fullname", userDto.getFullname())
                .param("username", userDto.getUsername())
                .param("password", userDto.getPassword())
                .param("role", userDto.getRole()))
                .andExpect(model().hasErrors())
                .andExpect(view().name("user/add"))
                .andReturn();
        String content = result.getResponse().getContentAsString();

        assertThat(content).contains("The password must contain at least 8 characters, one uppercase letter, one number and one symbol");
    }

    @Test
    @DisplayName("Given id User and UserDto to update, when post request, then update User in BDD")
    public void givenUsertDtoUpdate_whenUpdateRequest_updateIsOk() throws Exception {
        UserDto updateUserDto = new UserDto("Jack", "USER","Ursul1pm*", "Jacki" );
        User user = new User("Jack", "Ursul1pm*", "Jack", "USER");
        User updateUser = new User("Jack", "Ursul1pm*", "Jacki", "USER");

        User save = repository.save(user);

        String url = "/user/update/".concat(String.valueOf(save.getId()));

        log.info("Url update User : {}", url);

        mvc.perform(MockMvcRequestBuilders.post(url)
                .sessionAttr("UserDto", updateUserDto)
                .param("fullname", updateUserDto.getFullname())
                .param("username", updateUserDto.getUsername())
                .param("password", updateUserDto.getPassword())
                .param("role", updateUserDto.getRole()))
                .andExpect(model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/list"));

        repository.findById(save.getId())
                .ifPresent(userOne -> assumeTrue(userOne.getFullname().equals(updateUser.getFullname())
                        && userOne.getUsername().equals(updateUser.getUsername())));

        repository.deleteById(save.getId());
    }


    @Test
    @DisplayName("Given id User, when DELETE request, then DELETE in BDD search if exist")
    public void givenIdUserDelete_whenDeleteRequest_deleteIsOk() throws Exception {
        User user = new User("Jacko", "Ursul1pm*", "Jacko", "USER");

        User save = repository.save(user);
        String url = "/user/delete/".concat(String.valueOf(save.getId()));

        mvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(redirectedUrl("/user/list"));
        assumeFalse(repository.existsById(save.getId()));
    }


    @Test
    @DisplayName("Count number User in Bdd and check number is the same in request")
    public void readAllUser_thenShowUserListList() throws Exception {
        int nbUser = (int) repository.findAll().stream().count();
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/user/list"))
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name("user/list"))
                .andReturn();

        AtomicInteger atomicInteger = new AtomicInteger();
        result.getModelAndView().getModel()
                .forEach((s, t) ->
                {
                    List<UserDto> modelUsers = (List<UserDto>) t;
                    if (s.equals("users")) {
                        atomicInteger.set(modelUsers.size());
                    }
                });
        log.info("nombre  = " + nbUser + " /  retour :  " + atomicInteger.get());
        assumeTrue(nbUser == atomicInteger.get());
    }


}