package com.jasu.loginregister.Jwt;

import com.jasu.loginregister.Entity.AccessToken;
import com.jasu.loginregister.Entity.DefinitionEntity.DeRole;
import com.jasu.loginregister.Entity.User;
import com.jasu.loginregister.Entity.UserRole;
import com.jasu.loginregister.Jwt.Principal.UserPrincipal;
import com.jasu.loginregister.Jwt.Principal.UserPrincipalServiceImpl;
import com.jasu.loginregister.Service.AccessTokenService;
import com.jasu.loginregister.Service.UserRoleService;
import com.jasu.loginregister.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class AuthTokenFilter extends OncePerRequestFilter {
  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserRoleService userRoleService;

  @Autowired
  private AccessTokenService accessTokenService;

  @Autowired
  private UserPrincipalServiceImpl userDetailsService;

  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String jwt = parseJwt(request);
      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        AccessToken accessToken = accessTokenService.findByToken(jwt);
        if (accessToken != null){
          String userId = jwtUtils.getUserIdFromJwtToken(jwt);
          Set<GrantedAuthority> authorities = new HashSet<>();
          UserPrincipal userPrincipal = userDetailsService.loadUserById(Long.parseLong(userId));

          Set<UserRole>userRoles  = userRoleService.getListUserRole(userPrincipal.getId());
//          if(!userPrincipal.isEnabled()){
//            authorities.add(new SimpleGrantedAuthority(DeRole.USER.getAuthority()));
//          }
//          else {
//
//          }
          for (UserRole userRole: userRoles){
            authorities.add(new SimpleGrantedAuthority(userRole.getRoleKey()));
          }
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null,
                  authorities);
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
    } catch (Exception e) {
      logger.error("Cannot set user authentication: {}", e.getMessage());
    }

    filterChain.doFilter(request, response);
  }

  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7, headerAuth.length());
    }

    return null;
  }
}
