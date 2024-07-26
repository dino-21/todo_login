package com.example.demo.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * 이 클래스는 JWT 인증 필터로, 들어오는 요청에서 JWT 토큰을 추출하고 검증하여
 * 인증된 사용자를 SecurityContextHolder에 설정하는 역할을 한다.
 */

@Slf4j
@Component
public class JwtAuthenticationFilter  extends OncePerRequestFilter {

    @Autowired
    private TokenProvider tokenProvider;

    /**
     * 이 메서드는 들어오는 요청을 필터링하는 메서드로, JWT 토큰을 검증하고
     * 인증된 사용자를 SecurityContextHolder에 설정한다.
     *
     * @param request HttpServletRequest 객체
     * @param response HttpServletResponse 객체
     * @param filterChain FilterChain 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException 입출력 예외
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // 요청에서 토큰 가져오기.
            String token = parseBearerToken(request);
            log.info("Filter is running...");  // 필터가 실행 중임을 로그에 기록.
            // 토큰 검사하기. JWT이므로 인가 서버에 요청 하지 않고도 검증 가능.
            if (token != null && !token.equalsIgnoreCase("null")) {
                // userId 가져오기. 위조 된 경우 예외 처리 된다.
                String userId = tokenProvider.validateAndGetUserId(token);
                log.info("Authenticated user ID : " + userId ); // 인증된 사용자 ID 로그에 기록.
                // 인증 완료; SecurityContextHolder에 등록해야 인증된 사용자로 간주됨
                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId, // <- AuthenticationPrincipal (또는 principal)
                        null, //
                        AuthorityUtils.NO_AUTHORITIES
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);
            }
        } catch (Exception ex) { // 인증 설정 실패 시 에러 로그 기록.
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response); // 다음 필터 실행.

    }


    /**
     * 이 메서드는 Http 요청의 헤더를 파싱해 Bearer 토큰을 추출하는 역할을 한다.
     * JWT 토큰은 클라이언트가 서버에 인증된 사용자임을 증명하기 위해 사용된다.
     * 클라이언트가 서버에 요청을 보낼 때 Authorization 헤더에 Bearer 토큰을 포함시킨다.
     * 이 메서드는 해당 헤더에서 Bearer 토큰을 추출하여 반환한다.
     *  추출된 토큰은 이후의 인증 과정에서 사용된다.
     *
     * @param request HttpServletRequest 객체
     * @return 추출된 Bearer 토큰 또는 null
     */

    private String parseBearerToken(HttpServletRequest request) {
        // Http 요청의 헤더를 파싱해 Bearer 토큰을 리턴한다.
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);  // "Bearer " 문자열을 제거하고 토큰 값만 리턴.
        }
        return null; // 토큰이 없거나 형식이 맞지 않으면 null 리턴.
    }
}
