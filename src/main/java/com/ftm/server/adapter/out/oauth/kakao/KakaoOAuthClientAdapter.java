package com.ftm.server.adapter.out.oauth.kakao;

import static com.ftm.server.common.consts.StaticConsts.AUTHORIZATION_GRANT_TYPE;
import static com.ftm.server.common.consts.StaticConsts.AUTHORIZATION_HEADER_PREFIX;

import com.ftm.server.application.command.auth.KakaoLoginCommand;
import com.ftm.server.application.port.out.oauth.SocialOAuthClientPort;
import com.ftm.server.common.annotation.Adapter;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.infrastructure.oauth.kakao.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Adapter
@RequiredArgsConstructor
public class KakaoOAuthClientAdapter
        implements SocialOAuthClientPort<KakaoLoginCommand, KakaoAuthUser> {

    private final KakaoProperties kakaoProperties;
    private final WebClient webClient;

    @Override
    public KakaoAuthUser authenticate(KakaoLoginCommand command) {
        // Access Token 요청
        String redirectUri = kakaoProperties.getRedirectUriByEnv(command.getRedirectEnv());
        KakaoTokenResponse token = getKakaoToken(command.getAuthorizationCode(), redirectUri);

        // 카카오 유저 정보 요청
        KakaoUserInfoResponse userInfo = getKakaoUserInfo(token.getAccessToken());

        return KakaoAuthUser.from(userInfo.getId());
    }

    private KakaoTokenResponse getKakaoToken(String authorizationCode, String redirectUri) {
        return requestToken(authorizationCode, redirectUri).block();
    }

    private KakaoUserInfoResponse getKakaoUserInfo(String accessToken) {
        return requestUserInfo(accessToken).block();
    }

    private Mono<KakaoTokenResponse> requestToken(String authorizationCode, String redirectUri) {
        return webClient
                .post()
                .uri(kakaoProperties.getTokenUri())
                .body(
                        BodyInserters.fromFormData("grant_type", AUTHORIZATION_GRANT_TYPE)
                                .with("client_id", kakaoProperties.getClientId())
                                .with("redirect_uri", redirectUri)
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
