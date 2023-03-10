package com.c103.dolbom.user.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.c103.dolbom.Entity.Member;
import com.c103.dolbom.repository.MemberRepository;
import com.c103.dolbom.user.auth.PrincipalDetails;
import com.c103.dolbom.user.common.JwtProperties;
import com.c103.dolbom.user.dto.RefreshToken;
import com.c103.dolbom.user.repository.RefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final MemberRepository memberRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository, RefreshTokenRepository refreshTokenRepository) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.info("JwtAuthorization ??????");
        String servletPath = request.getServletPath();
        String header = request.getHeader(JwtProperties.ACCESS_HEADER_STRING);
        log.debug(servletPath);
        String email = " ";

        // header??? ????????? ??????
        if (servletPath.equals("/api/users/login") || servletPath.equals("/users/token/refresh")) {
            chain.doFilter(request, response);
        } else if(header == null || !header.startsWith(JwtProperties.TOKEN_HEADER_PREFIX)) {
            // ???????????? ????????? ??????????????? ????????? 400 ??????
            chain.doFilter(request, response);
        } else {
            try {
                log.debug("header : {}", header);

                //JWT ????????? ????????? ?????? ???????????? ??????????????? ??????
                String token = request.getHeader(JwtProperties.ACCESS_HEADER_STRING)
                        .replace("Bearer ", "");

                // ?????? ?????? (?????? ???????????? ????????? AuthenticationManager??? ?????? ??????)
                // ?????? SecurityContext??? ?????????????????? ????????? ????????? ???????????? UserDetailsService??? ?????? loadByUsername??? ?????????.
                email = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET_KEY)).build().verify(token)
                        .getClaim("email").asString();

                // ??????????????? ????????? ???
                if (email != null) {
                    Member member = memberRepository.findByEmail(email)
                            .orElseThrow(() -> new UsernameNotFoundException("???????????? ?????? ??? ????????????."));
                    log.debug(member.toString());

                    // ????????? ?????? ????????? ???. ????????? ?????? ???????????? ?????? ????????? ??????????????? ??????????????? ?????? ????????? ??????
                    // ????????? ?????? ????????? ???????????? Authentication ????????? ????????? ????????? ?????? ????????? ??????!
                    // Jwt?????? ????????? ????????? ????????? ???????????? Authentication ????????? ????????? ??????.
                    PrincipalDetails principalDetails = new PrincipalDetails(member);
                    Authentication authentication =
                            new UsernamePasswordAuthenticationToken(
                                    principalDetails, //????????? ?????????????????? DI?????? ??? ??? ???????????? ??????.
                                    null, // ??????????????? ???????????? null ??????
                                    principalDetails.getAuthorities());

                    for (GrantedAuthority ga : principalDetails.getAuthorities()) {
                        log.debug("role : {}", ga.toString());
                    }

                    // ????????? ??????????????? ????????? ???????????? Authentication ????????? ??????
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

                chain.doFilter(request, response);
            } catch(TokenExpiredException e) {
                log.info("TokenExpired : ");
                String token = request.getHeader(JwtProperties.REFRESH_HEADER_STRING)
                        .replace("Bearer ", "");

                try {
                    //refreshToken ??????
                    email = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET_KEY)).build().verify(token).getSubject();
                } catch(TokenExpiredException te) {
                    logger.info("CustomAuthorizationFilter : Refresh Token??? ?????????????????????.");
                    response.setContentType(APPLICATION_JSON_VALUE);
                    response.setCharacterEncoding("utf-8");
                    new ObjectMapper().writeValue(response.getWriter(), new ResponseEntity<String>("Refresh Token??? ?????????????????????.", HttpStatus.UNAUTHORIZED));
                }

                Optional<RefreshToken> optMember = refreshTokenRepository.findById(email);

                if(!token.equals(optMember.get().getRefreshToken())) {
                    logger.info("CustomAuthorizationFilter : Token??? ???????????????.");
                    response.setContentType(APPLICATION_JSON_VALUE);
                    response.setCharacterEncoding("utf-8");
                    new ObjectMapper().writeValue(response.getWriter(), new ResponseEntity<String>("???????????? ?????? Refresh Token?????????.", HttpStatus.UNAUTHORIZED));
                } else {
                    Member member = memberRepository.findByEmail(optMember.get().getEmail()).get();
                    // RSA ?????? ????????? Hash ????????????
                    String accessToken = JWT.create()
                            .withSubject(member.getEmail())
                            .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXP_TIME))
                            .withClaim("name", member.getName())
//                .withClaim("role", principalDetails.getMember().getRole().toString())
                            .sign(Algorithm.HMAC512(JwtProperties.SECRET_KEY));


                    PrincipalDetails principalDetails = new PrincipalDetails(member);
                    Authentication authentication =
                            new UsernamePasswordAuthenticationToken(
                                    principalDetails, //????????? ?????????????????? DI?????? ??? ??? ???????????? ??????.
                                    null, // ??????????????? ???????????? null ??????
                                    principalDetails.getAuthorities());

                    for (GrantedAuthority ga : principalDetails.getAuthorities()) {
                        log.debug("role : {}", ga.toString());
                    }

                    // ????????? ??????????????? ????????? ???????????? Authentication ????????? ??????
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    chain.doFilter(request, response);
                }


            } catch(Exception e) {
                logger.info("CustomAuthorizationFilter : JWT ????????? ?????????????????????. message : " + e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("utf-8");
                new ObjectMapper().writeValue(response.getWriter(), new ResponseEntity<String>("???????????? : " + e.getMessage(), HttpStatus.BAD_REQUEST));
            }
        }

    }
}
