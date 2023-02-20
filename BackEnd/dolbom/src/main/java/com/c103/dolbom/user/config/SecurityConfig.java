package com.c103.dolbom.user.config;

import com.c103.dolbom.repository.MemberRepository;
import com.c103.dolbom.user.filter.JwtAuthenticationFilter;
import com.c103.dolbom.user.filter.JwtAuthorizationFilter;
import com.c103.dolbom.user.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


//    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final CorsConfig corsConfig;
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager(), refreshTokenRepository);
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/users/login");

        http
                .addFilter(corsConfig.corsFilter()) // cors 처리
                .csrf().disable() // csrf 처리
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session 사용 x
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilter(jwtAuthenticationFilter)
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), memberRepository, refreshTokenRepository))
                .authorizeRequests()
                .antMatchers("/")
                .authenticated()
                .antMatchers("/api/v1/user/**")
//			.access("hasRole('ROLE_admin') or hasRole('ROLE_user')")
                .hasAnyAuthority("admin","user")
                .antMatchers("/api/v1/admin/**")
//				.access("hasRole('admin')")
                .hasAuthority("admin")
//                .antMatchers("/api/file/**" , "api/folder/**", "api/client/**", "api/upload")
//                .hasRole("COUNSELOR")
                .anyRequest().permitAll();

    }
}

