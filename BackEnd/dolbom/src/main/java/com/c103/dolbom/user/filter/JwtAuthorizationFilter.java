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
        log.info("JwtAuthorization 시작");
        String servletPath = request.getServletPath();
        String header = request.getHeader(JwtProperties.ACCESS_HEADER_STRING);
        log.debug(servletPath);
        String email = " ";

        // header가 있는지 확인
        if (servletPath.equals("/api/users/login") || servletPath.equals("/users/token/refresh")) {
            chain.doFilter(request, response);
        } else if(header == null || !header.startsWith(JwtProperties.TOKEN_HEADER_PREFIX)) {
            // 토큰값이 없거나 정상적이지 않다면 400 오류
            chain.doFilter(request, response);
        } else {
            try {
                log.debug("header : {}", header);

                //JWT 토큰을 검증을 해서 정상적인 사용자인지 확인
                String token = request.getHeader(JwtProperties.ACCESS_HEADER_STRING)
                        .replace("Bearer ", "");

                // 토큰 검증 (이게 인증이기 때문에 AuthenticationManager도 필요 없음)
                // 내가 SecurityContext에 직접접근해서 세션을 만들때 자동으로 UserDetailsService에 있는 loadByUsername이 호출됨.
                email = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET_KEY)).build().verify(token)
                        .getClaim("email").asString();

                // 정상적으로 서명이 됨
                if (email != null) {
                    Member member = memberRepository.findByEmail(email)
                            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
                    log.debug(member.toString());

                    // 인증은 토큰 검증시 끝. 인증을 하기 위해서가 아닌 스프링 시큐리티가 수행해주는 권한 처리를 위해
                    // 아래와 같이 토큰을 만들어서 Authentication 객체를 강제로 만들고 그걸 세션에 저장!
                    // Jwt토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어 준다.
                    PrincipalDetails principalDetails = new PrincipalDetails(member);
                    Authentication authentication =
                            new UsernamePasswordAuthenticationToken(
                                    principalDetails, //나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
                                    null, // 패스워드는 모르니까 null 처리
                                    principalDetails.getAuthorities());

                    for (GrantedAuthority ga : principalDetails.getAuthorities()) {
                        log.debug("role : {}", ga.toString());
                    }

                    // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

                chain.doFilter(request, response);
            } catch(TokenExpiredException e) {
                log.info("TokenExpired : ");
                String token = request.getHeader(JwtProperties.REFRESH_HEADER_STRING)
                        .replace("Bearer ", "");

                try {
                    //refreshToken 만료
                    email = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET_KEY)).build().verify(token).getSubject();
                } catch(TokenExpiredException te) {
                    logger.info("CustomAuthorizationFilter : Refresh Token이 만료되었습니다.");
                    response.setContentType(APPLICATION_JSON_VALUE);
                    response.setCharacterEncoding("utf-8");
                    new ObjectMapper().writeValue(response.getWriter(), new ResponseEntity<String>("Refresh Token이 만료되었습니다.", HttpStatus.UNAUTHORIZED));
                }

                Optional<RefreshToken> optMember = refreshTokenRepository.findById(email);

                if(!token.equals(optMember.get().getRefreshToken())) {
                    logger.info("CustomAuthorizationFilter : Token이 이상합니다.");
                    response.setContentType(APPLICATION_JSON_VALUE);
                    response.setCharacterEncoding("utf-8");
                    new ObjectMapper().writeValue(response.getWriter(), new ResponseEntity<String>("유효하지 않은 Refresh Token입니다.", HttpStatus.UNAUTHORIZED));
                } else {
                    Member member = memberRepository.findByEmail(optMember.get().getEmail()).get();
                    // RSA 방식 아니고 Hash 암호방식
                    String accessToken = JWT.create()
                            .withSubject(member.getEmail())
                            .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXP_TIME))
                            .withClaim("name", member.getName())
//                .withClaim("role", principalDetails.getMember().getRole().toString())
                            .sign(Algorithm.HMAC512(JwtProperties.SECRET_KEY));


                    PrincipalDetails principalDetails = new PrincipalDetails(member);
                    Authentication authentication =
                            new UsernamePasswordAuthenticationToken(
                                    principalDetails, //나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
                                    null, // 패스워드는 모르니까 null 처리
                                    principalDetails.getAuthorities());

                    for (GrantedAuthority ga : principalDetails.getAuthorities()) {
                        log.debug("role : {}", ga.toString());
                    }

                    // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    chain.doFilter(request, response);
                }


            } catch(Exception e) {
                logger.info("CustomAuthorizationFilter : JWT 토큰이 잘못되었습니다. message : " + e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("utf-8");
                new ObjectMapper().writeValue(response.getWriter(), new ResponseEntity<String>("인가필터 : " + e.getMessage(), HttpStatus.BAD_REQUEST));
            }
        }

    }
}
