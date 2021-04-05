package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.services.UserService;
import com.nnk.springboot.services.exceptions.UserExistException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Log4j2
@Controller
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * Send UserDto list.
     *
     * @param model
     * @return The URI to the user/list
     */
    @RequestMapping("/user/list")
    public String home(Model model) {
        model.addAttribute("users", userService.readAll());
        return "user/list";
    }


    /**
     * Send UserDto to save.
     *
     * @param model
     * @return The URI to the user/add
     */
    @GetMapping("/user/add")
    public String addUser(Model model) {
        log.info("GET : /user/add");
        model.addAttribute("userDto", new UserDto());
        return "user/add";
    }


    /**
     *
     * Save a new UserDto
     *
     * @param userDto new user to save
     * @param result check validation
     * @param model the entity
     * @returnThe URI to the user/add if result has errors.
     * Else, redirects to user/list endpoint
     * if UserName exist, URI to the user/add with error UserExistException
     *
     */
    @PostMapping("/user/validate")
    public String validate(@Valid UserDto userDto, BindingResult result, Model model) {
        log.info("POST : /user/validate");
        if (!result.hasErrors()) {
            try {
                userService.save(userDto);
                log.info("POST : /user/add - SUCCES");
                model.addAttribute("users", userService.readAll());
                return "redirect:/user/list";
            } catch (UserExistException userExistException) {
                log.error("POST : /user/add - ERROR : {}",userExistException.getMessage());
                model.addAttribute("userExit", userExistException.getMessage());
                return "user/add";
            }
        }
        return "user/add";
    }

    /**
     * Send to update form an existing user
     *
     * @param id to update user
     * @param model the entity
     * @return the URI to the user/update
     */
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        UserDto user = userService.readByid(id);
        user.setPassword("");
        model.addAttribute("user", user);
        return "user/update";
    }


    /**
     * user is update
     *
     * @param id to update user
     * @param UserDto the entity update
     * @param result check validation
     * @param model the entity
     * @return The URI to the trade/update, if result has errors.
     * Else, redirects to /trade/list endpoint
     * if UserName exist, URI to the user/add with error UserExistException
     *
     */
    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid UserDto userDto,
                             BindingResult result, Model model) {
        log.debug("POST : /user/update/{}", id);
        if (result.hasErrors()) {
            log.info("POST : /user/update/{} - ERROR", id);
            model.addAttribute("user", userDto);
            return "user/update";
        }
        try {
            userService.update(id, userDto);
            model.addAttribute("users", userService.readAll());
            log.info("POST : /user/update/{} - SUCCESS", id);
            return "redirect:/user/list";

        } catch (UserExistException userExistException) {
            log.error("Update user exist {}",userExistException.getMessage());
            model.addAttribute("userExistUpdate", userExistException.getMessage());
        }
        model.addAttribute("user", userDto);
        return "user/update";
    }


    /**
     * Find User by Id and delete the user
     *
     * @param id to delete trade
     * @param model list entity
     * @return The URI to the user/list
     *
     */
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        log.debug("DELETE : /user/delete/{}", id);
        userService.delete(id);
        log.info("DELETE : /user/delete/{} - SUCCESS", id);
        model.addAttribute("users", userService.readAll());
        return "redirect:/user/list";
    }
}
