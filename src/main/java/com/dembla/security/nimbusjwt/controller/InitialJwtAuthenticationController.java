package com.dembla.security.nimbusjwt.controller;

import com.dembla.security.nimbusjwt.nimbus.jose.NimbusJoseJwtRequest;
import com.dembla.security.nimbusjwt.nimbus.jose.NimbusJoseJwtResponse;
import com.dembla.security.nimbusjwt.user.CustomSpringUserDetailsImpl;
import com.dembla.security.nimbusjwt.util.JoseJwtClaimGeneratorUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class InitialJwtAuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JoseJwtClaimGeneratorUtil joseJwtClaimGeneratorUtil;
    private final CustomSpringUserDetailsImpl userDetailsService;


    public InitialJwtAuthenticationController(AuthenticationManager authenticationManager,
                                              JoseJwtClaimGeneratorUtil joseJwtClaimGeneratorUtil,
                                              CustomSpringUserDetailsImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.joseJwtClaimGeneratorUtil = joseJwtClaimGeneratorUtil;
        this.userDetailsService = userDetailsService;
    }


    @RequestMapping(value = "/authenticate/token", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody NimbusJoseJwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails employeeDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = joseJwtClaimGeneratorUtil.generateJwtToken(employeeDetails);

        return ResponseEntity.ok(new NimbusJoseJwtResponse(token, LocalDateTime.now().plusMinutes(1).toString()));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}


