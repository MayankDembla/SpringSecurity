package com.dembla.security.nimbusjwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomJoseJwtEntryPoint customJoseJwtEntryPoint;
    private final UserDetailsService jwtUserDetailsService;
    private final RequestResponseJwtChainFilter requestResponseJwtChainFilter;


    public WebSecurityConfig(CustomJoseJwtEntryPoint customJoseJwtEntryPoint, @Qualifier("customSpringUserDetailsImpl") UserDetailsService jwtUserDetailsService, RequestResponseJwtChainFilter requestResponseJwtChainFilter) {
        this.customJoseJwtEntryPoint = customJoseJwtEntryPoint;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.requestResponseJwtChainFilter = requestResponseJwtChainFilter;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // Can use other Encoders but using BCryptPasswordEncoder here
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
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
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf().disable()
                .authorizeRequests().antMatchers("/authenticate/token").permitAll().
                anyRequest().authenticated().and().
                exceptionHandling().authenticationEntryPoint(customJoseJwtEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(requestResponseJwtChainFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
