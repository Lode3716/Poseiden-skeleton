package com.nnk.springboot.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
public class ErrorController {

    @GetMapping("/403")
    public String showError403() {
        log.debug("GET Request on /403");

        return "/403";
    }
}
