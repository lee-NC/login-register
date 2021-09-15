package com.jasu.loginregister.Jwt;

import com.jasu.loginregister.Entity.Token;
import com.jasu.loginregister.Entity.UserRole;
import com.jasu.loginregister.Jwt.Principal.UserPrincipal;
import com.jasu.loginregister.Service.TokenService;
import com.jasu.loginregister.Service.UserRoleService;
import com.jasu.loginregister.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        UserPrincipal user = null;
        Token token = null;

        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Token ")) {
            String jwt = authorizationHeader.substring(6);
            user = jwtUtil.getUserFromToken(jwt);
            token = tokenService.findByToken(jwt);
        }

        if (null != user
                && null != token
                && token.getTokenExpDate().after(new Date())
                && !token.getDeleted()
                && user.getId().equals(Long.parseLong(token.getCreatedBy()))){
            Set<GrantedAuthority> authorities = new HashSet<>();
            Set<UserRole>userRoles  = userRoleService.getListUserRole(user.getId());
            for (UserRole userRole: userRoles){
                authorities.add(new SimpleGrantedAuthority(userRole.getRoleKey()));
                System.out.println(userRole.getRoleKey());
            }

//            User checkUser = userService.findByID(user.getId());
//            for (Role role: checkUser.getRoles()){
//                authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
//                System.out.println(role.getAuthority().toString());
//            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

}