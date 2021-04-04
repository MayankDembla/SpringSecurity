package com.dembla.security.nimbusjwt.nimbus.jose;

import lombok.Data;

@Data
public class NimbusJoseJwtRequest {

    private String username;
    private String password;
}
