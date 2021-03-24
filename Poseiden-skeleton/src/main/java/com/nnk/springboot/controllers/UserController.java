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

    @RequestMapping("/user/list")
    public String home(Model model) {
        model.addAttribute("users", userService.readAll());
        return "user/list";
    }

    @GetMapping("/user/add")
    public String addUser(Model model) {
        log.info("GET : /user/add");
        model.addAttribute("userDto", new UserDto());
        return "user/add";
    }

    @PostMapping("/user/validate")
    public String validate(@Valid UserDto userDto, BindingResult result, Model model) {
        log.debug("POST : /user/validate");
        if (!result.hasErrors()) {
            try {
            userService.save(userDto);
            log.info("POST : /user/add - SUCCES");
            model.addAttribute("users", userService.readAll());
                return "redirect:/user/list";
            } catch (UserExistException userExistException) {
                result.getFieldError("Pas good");
                result.hasErrors();
                return "user/add";
            }
            }


        return "user/add";
    }

    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        UserDto user = userService.readByid(id);
        user.setPassword("");
        model.addAttribute("user", user);
        return "user/update";
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid UserDto userDto,
                             BindingResult result, Model model) {
        log.debug("POST : /user/update/{}", id);
        if (result.hasErrors()) {
            log.info("POST : /user/update/{} - ERROR", id);
            return "user/update";
        }
        userService.update(id,userDto);
        model.addAttribute("users", userService.readAll());
        log.info("POST : /user/update/{} - SUCCESS", id);
        return "redirect:/user/list";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        log.debug("DELETE : /user/delete/{}", id);
        userService.delete(id);
        log.info("DELETE : /user/delete/{} - SUCCESS", id);
        model.addAttribute("users", userService.readAll());
        return "redirect:/user/list";
    }
}
