package com.ferdev.cafe.Jwt;

import com.ferdev.cafe.Security.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Service
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    private Claims claims = null;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username;
        String token;
        String authorizationHeader;

        if (request.getServletPath().matches("/user/login") || request.getServletPath().matches("/user/signup") ||
            request.getServletPath().matches("/user/forgot-password")) {
            filterChain.doFilter(request, response);
        }
        else {
            authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
                claims = jwtUtil.extractAllClaims(token);
                username= claims.get("subject").toString();
                // username= jwtUtil.extractUsername(token); (option)
                log.info("username {}", username);
                log.info("username {}", claims);
                log.info("claims {}", claims);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                    if (jwtUtil.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            }

            filterChain.doFilter(request, response);
        }
    }

    public boolean isAdmin(){
        return "admin".equalsIgnoreCase((String) claims.get("role"));
    }

    public boolean isUser(){
        return "user".equalsIgnoreCase((String) claims.get("role"));
    }

    public String getCurrentUser(){
        return (String) claims.get("sub");
    }

}
