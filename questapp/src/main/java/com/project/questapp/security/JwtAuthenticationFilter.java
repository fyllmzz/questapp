package com.project.questapp.security;

import com.project.questapp.services.UserDetailServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
     JwtTokenProvider jwtTokenProvider;
    @Autowired
    UserDetailServiceImpl userDetailsService;

    //frontdan back ende bi istek geldiğinde bir güvenlik kontrol (jwt kontrol) ekledim. doğru jwt ise içeri alcam değilse unauthorization.
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        try{
//            String jwtToken= extractJwtFromRequest(request);
//            if (StringUtils.hasText(jwtToken) && jwtTokenProvider.validateToken(jwtToken)){
//                Long id=jwtTokenProvider.getUserIdFromJwt(jwtToken);
//                UserDetails user=userDetailsService.loadUserById(id);
//                if(user != null){
//                    UsernamePasswordAuthenticationToken auth= new UsernamePasswordAuthenticationToken(user,null, user.getAuthorities());
//                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    // SecurityContextHolder , security ile alakalı seyleri localstorage gibi düşün. bi local thread abjesi oluşturuyor
//                    // Bu user için gerekli şeyleri tutuyor.
//                    SecurityContextHolder.getContext().setAuthentication(auth);
//
//                }
//            }
//        }catch (Exception e){
//            return;
//        }
//        filterChain.doFilter(request,response);
//    }
//
//    private String extractJwtFromRequest(HttpServletRequest request) {
//        //headerlarda istek atarken authorization altında bearer şeklinde göndercem
//        String bearer= request.getHeader("Authorization");
//        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer "))
//            return  bearer.substring("Bearer".length());
//        return null;
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwtToken = extractJwtFromRequest(request);
            if (StringUtils.hasText(jwtToken) && jwtTokenProvider.validateToken(jwtToken)) {
                Long id = jwtTokenProvider.getUserIdFromJwt(jwtToken);
                UserDetails user = userDetailsService.loadUserById(id); // UserDetailsService tipini kullanın
                if (user != null) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception e) {
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring("Bearer ".length());
        }
        return null;
    }
}
