package com.ftm.server.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.ftm.server.BaseTest;
import com.ftm.server.adapter.out.persistence.repository.UserRepository;
import com.ftm.server.application.port.in.user.UserHardDeleteUseCase;
import com.ftm.server.application.port.out.persistence.user.UpdateUserPort;
import com.ftm.server.application.port.out.s3.S3ImageDeletePort;
import com.ftm.server.domain.entity.User;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest
public class UserHardDeleteTest extends BaseTest {

    @Autowired private UserHardDeleteUseCase userHardDeleteUseCase;

    @Autowired private UserRepository userRepository; // 실제 JPA 리포지토리

    @Autowired private UpdateUserPort updateUserPort;

    @MockitoSpyBean private S3ImageDeletePort s3ImageDeletePort;

    @Test
    void hard_delete_성공() {
        // given
        User user1 = createTestUser("test1", "qwe123@");
        User user2 = createTestUser("test2", "qwe123@");

        user1.updateIsDeleted(true);
        user1.updateDeletedAt(LocalDateTime.now().minusDays(30));
        user2.updateIsDeleted(true);
        user2.updateDeletedAt(LocalDateTime.now().minusDays(30));

        updateUserPort.updateUser(user1);
        updateUserPort.updateUser(user2);
        userRepository.flush();

        doNothing().when(s3ImageDeletePort); // stub 생성, s3 직접 호출 x

        // when
        userHardDeleteUseCase.execute();

        // then
        assertThat(userRepository.findById(user1.getId())).isEmpty();
        assertThat(userRepository.findById(user2.getId())).isEmpty();
    }
}
