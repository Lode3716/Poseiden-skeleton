package com.nnk.springboot.controllers;

import com.nnk.springboot.config.jwt.JwtUtils;
import com.nnk.springboot.dto.LoginDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@Log4j2
@Controller
public class LoginController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Value("${poseidon.app.cookieExpirationSec}")
    private int cookieExpirationSec;

    /**
     * Displays login form.
     *
     * @param model makes a new LoginDTO object available to login HTML page
     * @return The reference to the login HTML page
     */
    @GetMapping("/login")
    public String showLoginForm(final Model model) {
        log.debug("GET Request on /login");

        model.addAttribute("loginDto", new LoginDto());

        log.info("GET Request on /login - SUCCESS");
        return "/login";
    }

    /**
     * Authenticates an user and provides a Token.
     *
     * @param loginDto user's login credentials
     * @param result   holds the result of validation and binding and contains errors that may have occurred
     * @param response HttpServletResponse instance
     * @return The reference to the login HTML page if result has no errors. Else, redirects to /bidList/list endpoint
     */
    @PostMapping("/signin")
    public String authentifictaionUser(@Valid final LoginDto loginDto, final BindingResult result,HttpServletResponse response) {

        if (result.hasErrors()) {
            log.error("Error(s): {}", result);
            return "/login";
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Creates a cookie and secures it
        Cookie cookie = new Cookie("Token",jwt);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(cookieExpirationSec);
        response.addCookie(cookie);

        return "redirect:/bidList/list";

    }
}
