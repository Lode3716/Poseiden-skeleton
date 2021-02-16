package com.nnk.springboot.config;

import com.nnk.springboot.config.jwt.AuthEntryPointJwt;
import com.nnk.springboot.config.jwt.AuthTokenFilter;
import com.nnk.springboot.config.services.UserDetailsServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
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
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/bidList/**", "/rating/**", "/ruleName/**", "/trade/**", "/curvePoint/**")
                .authenticated()
                .antMatchers("/user/**", "/admin/**").hasAuthority("ADMIN")
                .anyRequest().permitAll()
                .and().logout().logoutSuccessHandler(this::logoutSuccessHandler).permitAll()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and().exceptionHandling().accessDeniedHandler(this::accessDeniedHandler);
        //.anyRequest().authenticated();

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
     * Handles logout success.
     *
     * @param response       HttpServletResponse object
     * @param request        HttpServletRequest object
     * @param authentication Authentication object
     */
    private void logoutSuccessHandler(final HttpServletRequest request, final HttpServletResponse response,
                                      final Authentication authentication) throws IOException {
        log.debug("Inside SecurityConfiguration's logoutSuccessHandler method");

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
  /*
  Deuxième configuration


    private AccessDeniedHandler accessDeniedHandler;


    private DataSource dataSource;



    public void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .passwordEncoder(passwordEncoder())
                .dataSource(dataSource)
                .usersByUsernameQuery("select username,password,'true' as enabled  from users where username=?")
                .authoritiesByUsernameQuery("select username, role from users where username=?");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/bidList/**", "/rating/**", "/ruleName/**", "/trade/**", "/curvePoint/**")
                .authenticated()
                .antMatchers("/user/**", "/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .defaultSuccessUrl("/bidList/list")
                .and()
                .logout()
                .permitAll()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
*/
    /*
    Première configuration
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    public void globalConfig(AuthenticationManagerBuilder auth, DataSource dataSource) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username as principal, password as credentials from users where username =?")
                .authoritiesByUsernameQuery("select username as principal, role as role from users where username =?")
                .rolePrefix("ROLE_");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/ruleName/**","/rating/**","/curvePoint/**","/bidList/**","/user/**","/css/**","/js/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .defaultSuccessUrl("/bidList/list")
        .and()
        .logout()
        .permitAll()
        .and()
        .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }
*/


