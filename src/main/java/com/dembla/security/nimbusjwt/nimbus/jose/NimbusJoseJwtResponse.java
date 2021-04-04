package com.dembla.security.nimbusjwt.nimbus.jose;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class NimbusJoseJwtResponse {

    private final String yourToken;
    private final String expires;

}
