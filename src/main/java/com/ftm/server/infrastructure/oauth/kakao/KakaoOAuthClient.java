package com.ftm.server.infrastructure.oauth.kakao;

import static com.ftm.server.common.consts.StaticConsts.*;

import com.ftm.server.application.dto.command.KakaoAuthCommand;
import com.ftm.server.application.port.SocialAuthClientPort;
import com.ftm.server.common.annotation.InfraService;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@InfraService
@RequiredArgsConstructor
public class KakaoOAuthClient implements SocialAuthClientPort<KakaoAuthCommand, KakaoAuthUser> {

    private final KakaoProperties kakaoProperties;
    private final WebClient webClient;

    @Override
    public KakaoAuthUser authenticate(KakaoAuthCommand command) {
        // Access Token 요청
        KakaoTokenResponse token = getKakaoToken(command.getAuthorizationCode());

        // 카카오 유저 정보 요청
        KakaoUserInfoResponse userInfo = getKakaoUserInfo(token.getAccessToken());

        return KakaoAuthUser.from(userInfo.getId());
    }

    private KakaoTokenResponse getKakaoToken(String authorizationCode) {
        return requestToken(authorizationCode).block();
    }

    private KakaoUserInfoResponse getKakaoUserInfo(String accessToken) {
        return requestUserInfo(accessToken).block();
    }

    private Mono<KakaoTokenResponse> requestToken(String authorizationCode) {
        return webClient
                .post()
                .uri(kakaoProperties.getTokenUri())
                .body(
                        BodyInserters.fromFormData("grant_type", AUTHORIZATION_GRANT_TYPE)
                                .with("client_id", kakaoProperties.getClientId())
                                .with("redirect_uri", kakaoProperties.getRedirectUri())
                                .with("code", authorizationCode)
                                .with("client_secret", kakaoProperties.getClientSecret()))
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response ->
                                error(response, ErrorResponseCode.KAKAO_AUTH_TOKEN_EXCHANGE_FAILED))
                .bodyToMono(KakaoTokenResponse.class);
    }

    private Mono<KakaoUserInfoResponse> requestUserInfo(String accessToken) {
        return webClient
                .get()
                .uri(kakaoProperties.getUserInfoUri())
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_PREFIX + accessToken)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response ->
                                error(response, ErrorResponseCode.KAKAO_USER_PROFILE_FETCH_FAILED))
                .bodyToMono(KakaoUserInfoResponse.class);
    }

    private Mono<? extends Throwable> error(ClientResponse clientResponse, ErrorResponseCode code) {
        return clientResponse
                .bodyToMono(KakaoErrorResponse.class)
                .flatMap(
                        body -> {
                            log.error(
                                    "[KAKAO_ERROR] error: {}, description: {}",
                                    body.getError(),
                                    body.getErrorDescription());
                            return Mono.error(new CustomException(code));
                        });
    }
}
