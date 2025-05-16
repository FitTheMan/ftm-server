package com.ftm.server.application.service.user;

import com.ftm.server.application.command.user.DeleteBookmarkByIdCommand;
import com.ftm.server.application.command.user.DeleteBookmarkCommand;
import com.ftm.server.application.port.in.user.DeleteBookmarkUseCase;
import com.ftm.server.application.port.out.persistence.user.DeleteBookmarkPort;
import com.ftm.server.application.port.out.persistence.user.LoadBookmarkPort;
import com.ftm.server.application.query.FindBookmarkByUserIdAndPostIdQuery;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.Bookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteBookmarkService implements DeleteBookmarkUseCase {

    private final LoadBookmarkPort loadBookmarkPort;
    private final DeleteBookmarkPort deleteBookmarkPort;

    @Override
    @Transactional
    public void execute(DeleteBookmarkCommand command) {
        Bookmark bookmark =
                loadBookmarkPort
                        .loadBookmarkByUserIdAndPostId(
                                FindBookmarkByUserIdAndPostIdQuery.of(
                                        command.getUserId(), command.getPostId()))
                        .orElseThrow(
                                () -> new CustomException(ErrorResponseCode.BOOKMARK_NOT_FOUND));

        deleteBookmarkPort.deleteBookmarkById(DeleteBookmarkByIdCommand.of(bookmark.getId()));
    }
}
