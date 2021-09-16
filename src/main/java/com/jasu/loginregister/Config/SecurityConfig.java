//package com.jasu.loginregister.Config;
//
//import com.jasu.loginregister.Exception.ForbiddenException;
//import com.jasu.loginregister.Jwt.AuthEntryPointJwt;
//import com.jasu.loginregister.Jwt.JwtRequestFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.EnableAspectJAutoProxy;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableAspectJAutoProxy(proxyTargetClass = true)
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private JwtRequestFilter jwtRequestFilter;
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//
//        // Disable CSRF (cross site request forgery)
//        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
//                .csrf().disable();
//
//        // No session will be created or used by spring security
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        // Entry points
//        http.authorizeRequests()//
//                .antMatchers("/login").permitAll()//
//                .antMatchers("/registry").permitAll()//
//                // Disallow everything else...
//                .anyRequest().authenticated();
//
////        // If a user try to access a resource without having enough permissions
//        http.exceptionHandling().accessDeniedHandler(
//                (httpServletRequest, httpServletResponse, e) -> {throw new ForbiddenException("Access Denied");});
//
//        // Optional, if you want to test the API from a browser
////         http.httpBasic();
//
//        // If a user try to access a resource without having enough permissions
//        http.exceptionHandling().accessDeniedPage("/login");
//
//    }
//
//}
