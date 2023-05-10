package com.ebcr.configuration.filters;
import com.ebcr.models.User;
import com.ebcr.services.CustomUserDetailsService;
import com.ebcr.utils.TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import springfox.documentation.spi.service.contexts.SecurityContext;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
public class CustomAuthenticationFilter  extends OncePerRequestFilter {
    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try{
            String token = getTokenFromHeader(httpServletRequest);
            if(StringUtils.hasText(token) && tokenGenerator.validateToken(token)){
                   String username = tokenGenerator.getUserIdFromToken(token);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }catch (Exception exception){
            logger.error("Could not set user authentication in security " , exception);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
    private String getTokenFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
              return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
