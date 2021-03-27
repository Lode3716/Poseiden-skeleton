package com.nnk.springboot.config;

import com.nnk.springboot.config.jwt.AuthEntryPointJwt;
import com.nnk.springboot.config.jwt.AuthTokenFilter;
import com.nnk.springboot.services.UserDetailsServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Log4j2
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/bidList/**", "/rating/**", "/ruleName/**", "/trade/**", "/curvePoint/**")
                .authenticated()
                .antMatchers("/user/**").hasAuthority("ADMIN")
                .anyRequest().permitAll()
                .and().logout().logoutSuccessHandler(this::logoutSuccessHandler).permitAll()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and().exceptionHandling().accessDeniedHandler(this::accessDeniedHandler);

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    /**
     * Handles access denied.
     *
     * @param response HttpServletResponse object
     * @param request  HttpServletRequest object
     * @param e        AccessDeniedException object
     */
    private void accessDeniedHandler(final HttpServletRequest request, final HttpServletResponse response,
                                     final AccessDeniedException e)
            throws IOException {
        log.error("Access denied error: {}", e.getMessage());

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.sendRedirect("/403");
    }

    /**
     * Logout success.
     *
     * @param response       HttpServletResponse object
     * @param request        HttpServletRequest object
     * @param authentication Authentication object
     */
    private void logoutSuccessHandler(final HttpServletRequest request, final HttpServletResponse response,
                                      final Authentication authentication) throws IOException {

        // Retrieves the current cookie
        Cookie cookie = WebUtils.getCookie(request, "Token");
        if (cookie != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            // Invalidates the cookie
            cookie.setValue(null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
            // Redirects to home page after logout
            response.sendRedirect(request.getContextPath() + "?logout");
        }
    }

}

