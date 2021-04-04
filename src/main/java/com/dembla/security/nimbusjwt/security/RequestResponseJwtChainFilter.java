package com.dembla.security.nimbusjwt.security;

import com.dembla.security.nimbusjwt.user.CustomSpringUserDetailsImpl;
import com.dembla.security.nimbusjwt.util.JoseJwtClaimGeneratorUtil;
import com.nimbusds.jose.JOSEException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Component
//Using OncePerRequestFilter to guarantee a single execution per request dispatch, on any servlet container.
public class RequestResponseJwtChainFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_TOKEN = "Bearer ";
    private static final String TOKEN = "token";
    private static final String USERNAME = "username";

    private final CustomSpringUserDetailsImpl customSpringUserDetailsImpl;
    private final JoseJwtClaimGeneratorUtil joseJwtClaimGeneratorUtil;

    public RequestResponseJwtChainFilter(CustomSpringUserDetailsImpl customSpringUserDetailsImpl,
                                         JoseJwtClaimGeneratorUtil joseJwtClaimGeneratorUtil) {
        this.customSpringUserDetailsImpl = customSpringUserDetailsImpl;
        this.joseJwtClaimGeneratorUtil = joseJwtClaimGeneratorUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        try {

            final String requestTokenHeader = request.getHeader(AUTHORIZATION);

            //This will be used once we have token.
            String jwtToken = getTokenWithBearer(requestTokenHeader).get(TOKEN);
            String username = getTokenWithBearer(requestTokenHeader).get(USERNAME);

            // Once we get the token then only validate security context for currently authenticated principal, or an authentication request token.
            if (SecurityContextHolder.getContext().getAuthentication() == null && !StringUtils.isEmpty(jwtToken)) {

                UserDetails employeeDetails = customSpringUserDetailsImpl.loadUserByUsername(username);

                //Validate token and employee details.
                if (joseJwtClaimGeneratorUtil.validateTokenTimestamp(jwtToken, employeeDetails)) {

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            employeeDetails, null, employeeDetails.getAuthorities());

                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    //If no exception till here. User is validated add the authenticated principle.
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }

            }
        } catch (ParseException e) {

            throw new ServletException("ServletException caused by : Payload of JWS object not being a valid JSON object", e.getCause());
        } catch (JOSEException e) {

            throw new ServletException("JOSE exception for token: ", e.getCause());
        }

        chain.doFilter(request, response);
    }

    private Map<String, String> getTokenWithBearer(String requestTokenHeader) throws ServletException, JOSEException {

        Map<String, String> tokenWithUsername = new HashMap<>();

        if (requestTokenHeader != null && requestTokenHeader.startsWith(BEARER_TOKEN)) {

            try {

                tokenWithUsername.put(TOKEN, requestTokenHeader.substring(BEARER_TOKEN.length()));
                tokenWithUsername.put(USERNAME, joseJwtClaimGeneratorUtil.getUsernameFromToken(requestTokenHeader.substring(BEARER_TOKEN.length())));

            } catch (IllegalArgumentException | ParseException e) {
                throw new ServletException(e.getMessage());
            }
        }
        return tokenWithUsername;
    }
}
