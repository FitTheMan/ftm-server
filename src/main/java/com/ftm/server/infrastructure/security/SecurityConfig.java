package com.ftm.server.infrastructure.security;

import com.ftm.server.infrastructure.security.handler.PermissionDeniedHandler;
import com.ftm.server.infrastructure.security.handler.UnauthenticatedAccessHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final UnauthenticatedAccessHandler unauthenticatedAccessHandler;
    private final PermissionDeniedHandler permissionDeniedHandler;

    // CORS 에서 허용할 HTTP 메서드 목록
    public static final List<HttpMethod> CORS_ALLOWED_METHODS =
            List.of(
                    HttpMethod.GET,
                    HttpMethod.POST,
                    HttpMethod.PUT,
                    HttpMethod.PATCH,
                    HttpMethod.DELETE,
                    HttpMethod.HEAD);

    // CORS 에서 허용할 도메인 목록
    public static final List<String> CORS_ALLOWED_ORIGINS =
            List.of(
                    "http://localhost:8080", // 로컬 환경 서버 도메인
                    "https://dev-api.fittheman.site", // 개발 환경 서버 도메인
                    "https://fittheman.site"); // 개발 환경 클라이언트 도메인

    private static final String[] GET_ANONYMOUS_MATCHERS = {"/api/users/email/duplication"};

    private static final String[] POST_ANONYMOUS_MATCHERS = {"/api/users/email/authentication"};

    private static final String[] ANONYMOUS_MATCHERS = {"/docs/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // csrf 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                // http basic 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)
                // 폼로그인 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                // 로그아웃 비활성화
                .logout(AbstractHttpConfigurer::disable)
                // 세션 관리
                .sessionManagement(
                        session ->
                                session.sessionFixation()
                                        .migrateSession() // 세션 고정 보호
                                        .maximumSessions(1) // 동시 로그인 1개 제한
                                        .maxSessionsPreventsLogin(false)) // 기존 세션 만료 후 새 로그인 허용
                // 예외 핸들링
                .exceptionHandling(
                        exception ->
                                exception
                                        // 인증되지 않은 요청 예외 처리
                                        .authenticationEntryPoint(unauthenticatedAccessHandler)
                                        // 접근 권한 부족 예외 처리
                                        .accessDeniedHandler(permissionDeniedHandler))
                // cors 설정
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 경로 인가 설정
                .authorizeHttpRequests(
                        authorize -> {
                            authorize
                                    .requestMatchers(ANONYMOUS_MATCHERS)
                                    .permitAll() // 정적 리소스 경로 허용
                                    .requestMatchers(HttpMethod.GET, GET_ANONYMOUS_MATCHERS)
                                    .permitAll()
                                    .requestMatchers(HttpMethod.POST, POST_ANONYMOUS_MATCHERS)
                                    .permitAll();

                            // TODO: 요청 허용 특정 API 추가 (회원가입, 로그인 등)

                            // 그 외 모든 요청은 인증 필요
                            authorize.anyRequest().authenticated();
                        });

        return http.build();
    }

    // Password 암호화 설정
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // CORS 설정
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        CORS_ALLOWED_ORIGINS.forEach(config::addAllowedOriginPattern);
        CORS_ALLOWED_METHODS.forEach(config::addAllowedMethod);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
