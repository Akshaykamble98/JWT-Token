package com.myfirst.project.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.myfirst.project.impl.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if (requiresJwtAuthentication(requestURI)) {
            String requestTokenHeader = request.getHeader(AUTHORIZATION_HEADER);
            logger.warn("requestTokenHeader====="+requestTokenHeader);
            String jwtToken = null;

            // Check if the Authorization header contains the token and starts with "Bearer "
            if (requestTokenHeader != null && requestTokenHeader.startsWith(TOKEN_PREFIX)) {
                jwtToken = requestTokenHeader.substring(TOKEN_PREFIX.length()); // Extract the token without "Bearer "
            }

            // If token is null or not starting with "Bearer ", handle accordingly
            if (jwtToken == null || jwtToken.isEmpty()) {
                logger.warn("JWT Token does not begin with Bearer String");
                chain.doFilter(request, response);
                return;
            }

            // Validate and process the JWT token
            try {
                String username = jwtTokenUtil.getUsernameFromToken(jwtToken);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                    if (jwtTokenUtil.validateToken(jwtToken, userDetails.getUsername())) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception e) {
                logger.error("Error processing JWT token: " + e.getMessage());
            }
        }

        chain.doFilter(request, response);
    }

    private boolean requiresJwtAuthentication(String requestURI) {
        // Implement your logic here to determine if JWT authentication is needed
        // Example: Only apply JWT authentication to paths under /api
        return requestURI.startsWith("/api");
    }

}
