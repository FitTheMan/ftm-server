package com.ftm.server.adapter.out.persistence.adapter.user;

import com.ftm.server.adapter.out.persistence.mapper.*;
import com.ftm.server.adapter.out.persistence.model.*;
import com.ftm.server.adapter.out.persistence.repository.*;
import com.ftm.server.application.command.user.*;
import com.ftm.server.application.port.out.persistence.user.*;
import com.ftm.server.application.query.*;
import com.ftm.server.common.annotation.Adapter;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.domain.entity.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Slice;

@Adapter
@RequiredArgsConstructor
@Slf4j
public class UserDomainPersistenceAdapter
        implements LoadEmailVerificationLogPort,
                SaveEmailVerificationLogPort,
                UpdateEmailVerificationLogPort,
                CheckUserPort,
                SaveUserPort,
                SaveUserImagePort,
                LoadUserPort,
                LoadUserImagePort,
                UpdateUserPort,
                UpdateUserImagePort,
                LoadPostUserDomainPort,
                LoadPostImageUserDomainPort,
                UpdatePostUserDomainPort,
                DeleteUserImagePort,
                DeleteGroomingTestResultPort,
                DeleteUserPort,
                DeleteBookmarkPort,
                SaveBookmarkPort,
                CheckBookmarkPort,
                CheckPostPort {

    // repository
    private final EmailVerificationLogsRepository emailVerificationLogsRepository;
    private final UserRepository userRepository;
    private final GroomingLevelRepository groomingLevelRepository;
    private final UserImageRepository userImageRepository;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final BookmarkRepository bookmarkRepository;
    private final GroomingTestResultRepository groomingTestResultRepository;

    // mapper
    private final EmailVerificationLogsMapper emailVerificationLogsMapper;
    private final UserMapper userMapper;
    private final UserImageMapper userImageMapper;
    private final PostMapper postMapper;
    private final BookmarkMapper bookmarkMapper;
    private final PostImageMapper postImageMapper;

    @Override
    public Optional<EmailVerificationLogs> loadEmailVerificationLogByEmail(FindByEmailQuery query) {
        Optional<EmailVerificationLogsJpaEntity> emailVerificationLogsJpaEntity =
                emailVerificationLogsRepository.findByEmail(query.getEmail());
        return emailVerificationLogsJpaEntity.map(emailVerificationLogsMapper::toDomainEntity);
    }

    @Override
    public Optional<EmailVerificationLogs> loadEmailVerificationLogByEmailAndCode(
            EmailCodeVerificationQuery query) {
        Optional<EmailVerificationLogsJpaEntity> emailVerificationLogsJpaEntity =
                emailVerificationLogsRepository.findByVerificationCodeAndEmail(
                        query.getCode(), query.getEmail());
        return emailVerificationLogsJpaEntity.map(emailVerificationLogsMapper::toDomainEntity);
    }

    @Override
    public void saveEmailVerificationLogs(EmailVerificationLogCreationCommand command) {
        emailVerificationLogsRepository.save(
                emailVerificationLogsMapper.toJpaEntity(EmailVerificationLogs.from(command)));
    }

    @Override
    public void updateEmailVerificationLog(EmailVerificationLogs emailVerificationLogs) {
        EmailVerificationLogsJpaEntity emailVerificationLogsJpaEntity =
                emailVerificationLogsRepository.findById(emailVerificationLogs.getId()).get();
        emailVerificationLogsJpaEntity.updateFromDomainEntity(emailVerificationLogs);
    }

    @Override
    public Boolean checksUserByEmail(FindByEmailQuery query) {
        return userRepository.existsByEmail(query.getEmail());
    }

    @Override
    public User saveUser(User user) {
        UserJpaEntity userJpaEntity = userMapper.toJpaEntity(user, null);
        UserJpaEntity savedUser = userRepository.save(userJpaEntity);
        return userMapper.toDomainEntity(savedUser);
    }

    @Override
    public void saveUserDefaultImage(UserImage userImage) {
        UserJpaEntity userJpaEntity = userRepository.findById(userImage.getUserId()).get();
        UserImageJpaEntity userImageJpaEntity =
                userImageMapper.toJpaEntity(userImage, userJpaEntity);
        userImageRepository.save(userImageJpaEntity);
    }

    @Override
    public Boolean checksUserBySocialValue(FindBySocialValueQuery query) {
        return userRepository.existsBySocialIdAndSocialProvider(
                query.getSocialId(), query.getSocialProvider());
    }

    @Override
    public Boolean checksUserById(FindByUserIdQuery query) {
        return userRepository.existsById(query.getUserId());
    }

    @Override
    public User saveSocialUser(User user) {
        UserJpaEntity userJpaEntity = userMapper.toJpaEntity(user, null);
        UserJpaEntity savedUser = userRepository.save(userJpaEntity);
        return userMapper.toDomainEntity(savedUser);
    }

    @Override
    public User loadUserById(FindByUserIdQuery query) {
        UserJpaEntity userJpaEntity =
                userRepository
                        .findByIdAndIsDeleted(query.getUserId(), false)
                        .orElseThrow(() -> CustomException.USER_NOT_FOUND);
        return userMapper.toDomainEntity(userJpaEntity);
    }

    @Override
    public User loadUserByRole(FindUserByRoleQuery query) {
        UserJpaEntity userJpaEntity = userRepository.findByRole(query.getUserRole()).get();
        return userMapper.toDomainEntity(userJpaEntity);
    }

    @Override
    public List<User> loadUserByDeleteOption(FindUserByDeleteOptionQuery query) {
        return userRepository
                .findAllByDeletedBefore(
                        query.getIsDeleted(), query.getDeletedAt().atTime(23, 59, 59))
                .stream()
                .map(userMapper::toDomainEntity)
                .toList();
    }

    @Override
    public UserImage loadUserImageByUserId(FindByUserIdQuery query) {
        UserImageJpaEntity userImageJpaEntity =
                userImageRepository.findByUserId(query.getUserId()).orElse(null);
        if (userImageJpaEntity != null) {
            return userImageMapper.toDomainEntity(userImageJpaEntity);
        }
        return null;
    }

    @Override
    public void updateUser(User user) {
        UserJpaEntity savedUser =
                userRepository
                        .findById(user.getId())
                        .orElseThrow(() -> CustomException.USER_NOT_FOUND);
        GroomingLevelJpaEntity groomingLevelJpaEntity =
                user.getGroomingLevelId() == null
                        ? null
                        : groomingLevelRepository.findById(user.getGroomingLevelId()).orElse(null);

        savedUser.updateFromDomainEntity(user, groomingLevelJpaEntity);

        userRepository.save(savedUser);
    }

    @Override
    public void updateUserImage(UserImage userImage) {
        UserImageJpaEntity userImageJpaEntity =
                userImageRepository.findByUserId(userImage.getUserId()).orElse(null);
        if (userImageJpaEntity == null) {
            log.error("[USER_IMAGE_NOT_FOUND] : 사용자의 이미지 data를 찾을 수 없음.");
        }

        userImageJpaEntity.updateFromDomainEntity(userImage);
    }

    @Override
    public List<Post> loadPostListByUser(FindByUserIdQuery query) {
        return postRepository.findByUserId(query.getUserId()).stream()
                .map(postMapper::toDomainEntity)
                .toList();
    }

    @Override
    public Slice<Post> loadPostsByUserIdWithPaging(FindPostsByPagingQuery query) {
        return postRepository.findAllByUserIdWithPaging(query).map(postMapper::toDomainEntity);
    }

    @Override
    public List<PostImage> loadRepresentativeImagesByPostIds(FindByIdsQuery query) {
        return postImageRepository.findRepresentativeImagesByPostIdIn(query).stream()
                .map(postImageMapper::toDomainEntity)
                .toList();
    }

    @Override
    public void updatePostListBySystemUser(List<Post> postList) {
        UserJpaEntity systemUser = userRepository.findById(postList.get(0).getUserId()).get();

        Map<Long, Post> map = new HashMap<>();
        postList.forEach(p -> map.put(p.getId(), p));

        List<PostJpaEntity> postJpaEntityList =
                postRepository.findAllById(postList.stream().map(Post::getId).toList());

        postJpaEntityList.forEach(
                pj -> pj.updatePostForDomainEntity(map.get(pj.getId()), systemUser));
    }

    @Override
    public void deleteGroomingTestResultByUserList(
            DeleteGroomingTestResultByUserIdCommand command) {
        groomingTestResultRepository.deleteAllByUserIdList(command.getUserIdList());
    }

    @Override
    public List<String> deleteUserImageByUserList(DeleteUserImageByUserIdCommand command) {
        List<String> imageKeyList =
                userImageRepository.findAllByUserIdList(command.getUserIdList());
        userImageRepository.deleteAllByUserIdList(command.getUserIdList());
        return imageKeyList;
    }

    @Override
    public void deleteAllUserByIdList(DeleteAllUserByIdListCommand command) {
        userRepository.deleteAllByUserIdList(command.getUserIdList());
    }

    @Override
    public void deleteBookmarkByUserList(DeleteBookmarkByUserIdCommand command) {
        bookmarkRepository.deleteAllByUserIdList(command.getUserIdList());
    }

    @Override
    public Boolean saveBookmark(Bookmark bookmark) {
        // 이미 생성된 북마크인 경우  -> false
        // 새롭게 생성된 북마크인 경우  -> true
        int isCreated;
        try {
            isCreated = bookmarkRepository.saveOrUpdate(bookmark.getUserId(), bookmark.getPostId());
        } catch (
                DataIntegrityViolationException
                        e) { // on conflict로 문제 예방 했지만, 동시성 문제로 unique key 위반 에러 나는 경우, 에러 반환하지 않고
            // 그냥 처리(북마크 생성하지 않고 OK 반환)
            return false;
        }
        return isCreated == 1;
    }

    @Override
    public Boolean checkIfBookmarkExists(FindBookmarkByUserIdAndPostIdQuery query) {
        return bookmarkRepository.existsByUserIdAndPostId(query.getUserId(), query.getPostId());
    }

    @Override
    public Boolean checksPostById(FindByPostIdQuery query) {
        return postRepository.existsById(query.getPostId());
    }
}
