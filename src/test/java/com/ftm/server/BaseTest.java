package com.ftm.server;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyHeaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.HeadersModifyingOperationPreprocessor;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
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
}
