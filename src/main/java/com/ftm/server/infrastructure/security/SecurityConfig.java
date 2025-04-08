package com.ftm.server.infrastructure.security;

import com.ftm.server.adapter.out.security.UserPrincipalAdapter;
import com.ftm.server.infrastructure.security.handler.PermissionDeniedHandler;
import com.ftm.server.infrastructure.security.handler.UnauthenticatedAccessHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
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
                    HttpMethod.OPTIONS,
                    HttpMethod.HEAD);

    // CORS 에서 허용할 도메인 목록
    public static final List<String> CORS_ALLOWED_ORIGINS =
            List.of(
                    "http://localhost:8080", // 로컬 환경 서버 도메인
                    "https://dev-api.fittheman.site", // 개발 환경 서버 도메인
                    "http://localhost:3000", // 로컬 환경 클라이언트 도메인
                    "https://localhost:3000", // 로컬 환경 https 클라이언트 도메인
                    "https://fittheman.site"); // 개발 환경 클라이언트 도메인

    private static final String[] GET_ANONYMOUS_MATCHERS = {
        "/api/users/email/duplication", "/api/users/options", "/api/grooming/tests"
    };

    private static final String[] POST_ANONYMOUS_MATCHERS = {
        "/api/users/email/authentication",
        "/api/users/email/authentication/code",
        "/api/auth/login/**",
        "/api/users",
        "/api/users/social",
        "/api/grooming/tests/submission",
        "/api/grooming/tests"
    };

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
                // 인증되지 않은 요청에 대한 요청을 세션에 저장하지 않도록 하기 위한 설정
                .requestCache(AbstractHttpConfigurer::disable)
                // 시큐리티 컨텍스트 레포지토리 등록 (시큐리티 6.x 이상부터는 시큐리티가 자동으로 컨텍스트에 로드/저장해주지 않기 때문에 명시해줘야함)
                .securityContext(
                        context -> context.securityContextRepository(securityContextRepository()))
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

    // Spring Security 6 이상에서는 세션 기반 인증 유지를 위해 SecurityContextRepository 설정이 필요
    // HttpSession 기반 저장소를 통해 로그인 상태를 세션(Redis)에 자동 저장 및 복원
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    // 시큐리티 인증을 관리하는 AuthenticationManager 설정
    @Bean
    public AuthenticationManager authenticationManager(UserPrincipalAdapter userPrincipalService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userPrincipalService);
        provider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(provider);
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
