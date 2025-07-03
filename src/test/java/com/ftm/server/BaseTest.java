package com.ftm.server;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyHeaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftm.server.application.command.user.GeneralUserCreationCommand;
import com.ftm.server.application.port.out.persistence.auth.LoadUserForAuthPort;
import com.ftm.server.application.port.out.persistence.user.SaveUserImagePort;
import com.ftm.server.application.port.out.persistence.user.SaveUserPort;
import com.ftm.server.application.query.FindByEmailQuery;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.entity.UserImage;
import com.ftm.server.domain.enums.AgeGroup;
import com.ftm.server.domain.enums.HashtagCategory;
import com.ftm.server.domain.enums.UserRole;
import com.ftm.server.infrastructure.security.UserPrincipal;
import groovy.util.logging.Slf4j;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.HeadersModifyingOperationPreprocessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Slf4j
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest(classes = {ServerApplication.class})
@ActiveProfiles("test")
@ExtendWith({RestDocumentationExtension.class})
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE) // 테스트 시 내장된 인메모리 DB를 사용하지 않는다는 설정
@TestPropertySource(locations = "file:.env")
public class BaseTest {

    @Autowired protected MockMvc mockMvc;

    protected final ObjectMapper mapper = new ObjectMapper();

    @Autowired private SaveUserPort saveUserPort;
    @Autowired private SaveUserImagePort saveUserImagePort;
    @Autowired private LoadUserForAuthPort loadUserForAuthPort;

    @BeforeEach
    void setup(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc =
                MockMvcBuilders.webAppContextSetup(context)
                        .apply(SecurityMockMvcConfigurers.springSecurity()) // secrutiy filter 적용
                        .apply(documentationConfiguration(restDocumentation))
                        .build();
    }

    // 블필요한 header 제거 함수
    protected HeadersModifyingOperationPreprocessor getModifiedHeader() {
        return modifyHeaders()
                .remove("X-Content-Type-Options")
                .remove("X-XSS-Protection")
                .remove("Cache-Control")
                .remove("Pragma")
                .remove("Expires")
                .remove("Content-Length")
                .remove("X-Frame-Options")
                .remove("Vary");
    }

    // 사용자 생성 및 저장
    protected User createTestUser(String email, String password) {
        String nickname = "test " + UUID.randomUUID();
        User user =
                User.createGeneralUser(
                        GeneralUserCreationCommand.of(
                                email,
                                password,
                                nickname,
                                AgeGroup.FIFTIES,
                                List.of(HashtagCategory.PERFUME)));
        User testUser = saveUserPort.saveUser(user);
        saveUserImagePort.saveUserDefaultImage(UserImage.createUserImage(testUser.getId()));
        return testUser;
    }

    protected MockHttpSession createUserAndLogin() {
        return createUserAndLogin("test@gmail.com", "123456qwe!");
    }

    // test 사용자 생성 후 mock session 생성
    protected MockHttpSession createUserAndLogin(String email, String password) {

        // 사용자 생성
        User user = createTestUser(email, password);

        // session 생성
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        UserPrincipal.of(user),
                        null,
                        List.of(new SimpleGrantedAuthority(UserRole.USER.name())));
        context.setAuthentication(auth);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        return session;
    }

    protected MockHttpSession login(String email) {
        User user = loadUserForAuthPort.loadUserByEmail(FindByEmailQuery.of(email)).get();

        // session 생성
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        UserPrincipal.of(user),
                        null,
                        List.of(new SimpleGrantedAuthority(UserRole.USER.name())));
        context.setAuthentication(auth);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        return session;
    }

    public record SessionAndUser(MockHttpSession mockHttpSession, User user) {}

    // Session과 함께 User도 반환
    protected SessionAndUser createUserAndLoginAndReturnUser() {
        return createUserAndLoginAndReturnUser("test@gmail.com", "123456qwe!");
    }

    protected SessionAndUser createUserAndLoginAndReturnUser(String email, String password) {

        // 사용자 생성
        User user = createTestUser(email, password);

        // session 생성
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        UserPrincipal.of(user),
                        null,
                        List.of(new SimpleGrantedAuthority(UserRole.USER.name())));
        context.setAuthentication(auth);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        return new SessionAndUser(session, user);
    }

    protected MockHttpSession createAdminUserAndLogin() {
        String email = "admin@gmail.com";
        String password = "admin1234!";
        String nickname = "admintest";

        User admin = User.createAdminUser(email, password, nickname);

        // session 생성
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        UserPrincipal.of(admin),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + UserRole.ADMIN.name())));
        context.setAuthentication(auth);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        return session;
    }
}
