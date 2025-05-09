package com.ftm.server.application.service.user;

import com.ftm.server.application.command.user.CreateBookmarkCommand;
import com.ftm.server.application.port.in.user.CreateBookmarkUseCase;
import com.ftm.server.application.port.out.persistence.user.*;
import com.ftm.server.application.query.FindBookmarkByUserIdAndPostIdQuery;
import com.ftm.server.application.query.FindByPostIdQuery;
import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.Bookmark;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateBookmarkService implements CreateBookmarkUseCase {

    private final CheckUserPort checkUserPort;
    private final CheckPostPort checkPostPort;
    private final SaveBookmarkPort saveBookmarkPort;
    private final CheckBookmarkPort checkBookmarkPort;

    @Override
    @Transactional
    public Boolean execute(CreateBookmarkCommand command) {
        // user id와 post id 유효성 검사-> 없는 경우 exception 반환
        Boolean userExists =
                checkUserPort.checksUserById(FindByUserIdQuery.of(command.getUserId()));
        Boolean postExists =
                checkPostPort.checksPostById(FindByPostIdQuery.of(command.getPostId()));
        if (!userExists) {
            throw CustomException.USER_NOT_FOUND;
        }
        if (!postExists) {
            throw new CustomException(ErrorResponseCode.POST_NOT_FOUND);
        }

        // 북마크 기존에 존재하는지 확인(중복 생성 방지)
        if (checkBookmarkPort.checkIfBookmarkExists(
                FindBookmarkByUserIdAndPostIdQuery.of(command.getUserId(), command.getPostId()))) {
            return false;
        }
        // 북마크 생성 후 저장
        Bookmark bookmark = Bookmark.createBookmark(command.getUserId(), command.getPostId());

        // 북마크 존재하는지 검사 후 - 북마크 저장 과정 사이에 북마크가 생성되어 중복 생성되는 것을 막고자
        // sql 단에서 한번 더 검사 진행
        // -> 이미 동일한 북마크가 존재할 경우 do nothing & false 반환
        // -> 동일한 북마크가 없어서 새로 생성&저장한 경우 true 반환
        return saveBookmarkPort.saveBookmark(bookmark);
    }
}
