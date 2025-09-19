package com.ftm.server.adapter.out.persistence.adapter.post;

import com.ftm.server.adapter.out.persistence.mapper.*;
import com.ftm.server.adapter.out.persistence.model.*;
import com.ftm.server.adapter.out.persistence.repository.*;
import com.ftm.server.application.port.out.persistence.post.*;
import com.ftm.server.application.query.*;
import com.ftm.server.application.vo.post.*;
import com.ftm.server.common.annotation.Adapter;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class PostDomainPersistenceAdapter
        implements SavePostPort,
                SavePostImagePort,
                SavePostProductPort,
                SavePostProductImagePort,
                LoadPostPort,
                LoadPostImagePort,
                LoadPostProductPort,
                LoadPostProductImagePort,
                LoadUserForPostPort,
                LoadUserImageForPostPort,
                UpdatePostPort,
                UpdatePostProductPort,
                UpdatePostProductImagePort,
                DeletePostPort,
                DeletePostImagePort,
                DeletePostProductPort,
                DeletePostProductImagePort,
                LoadPostWithBookmarkCountPort,
                LoadUserForPostDomainPort {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final PostProductRepository postProductRepository;
    private final PostProductImageRepository postProductImageRepository;
    private final UserRepository userRepository;
    private final UserImageRepository userImageRepository;

    private final PostMapper postMapper;
    private final PostImageMapper postImageMapper;
    private final PostProductMapper postProductMapper;
    private final PostProductImageMapper postProductImageMapper;
    private final UserMapper userMapper;
    private final UserImageMapper userImageMapper;

    @Override
    public Post savePost(Post post) {
        UserJpaEntity userJpaEntity =
                userRepository
                        .findById(post.getUserId())
                        .orElseThrow(() -> CustomException.USER_NOT_FOUND);
        PostJpaEntity postJpaEntity =
                postRepository.save(postMapper.toJpaEntity(post, userJpaEntity));

        return postMapper.toDomainEntity(postJpaEntity);
    }

    @Override
    public List<PostImage> savePostImages(List<PostImage> postImages) {
        List<PostImageJpaEntity> postImageJpaEntities =
                postImages.stream()
                        .map(
                                postImage -> {
                                    PostJpaEntity postJpaEntity =
                                            postRepository
                                                    .findById(postImage.getPostId())
                                                    .orElseThrow(
                                                            () ->
                                                                    new CustomException(
                                                                            ErrorResponseCode
                                                                                    .POST_NOT_FOUND));

                                    return postImageMapper.toJpaEntity(postImage, postJpaEntity);
                                })
                        .toList();

        return postImageRepository.saveAll(postImageJpaEntities).stream()
                .map(postImageMapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<PostProduct> savePostProducts(List<PostProduct> postProducts) {
        List<PostProductJpaEntity> productJpaEntities =
                postProducts.stream()
                        .map(
                                postProduct -> {
                                    PostJpaEntity postJpaEntity =
                                            postRepository
                                                    .findById(postProduct.getPostId())
                                                    .orElseThrow(
                                                            () ->
                                                                    new CustomException(
                                                                            ErrorResponseCode
                                                                                    .POST_NOT_FOUND));
                                    return postProductMapper.toJpaEntity(
                                            postProduct, postJpaEntity);
                                })
                        .toList();

        return postProductRepository.saveAll(productJpaEntities).stream()
                .map(postProductMapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<PostProductImage> savePostProductImages(List<PostProductImage> productImages) {
        List<PostProductImageJpaEntity> postProductImageJpaEntities =
                productImages.stream()
                        .map(
                                postProductImage -> {
                                    PostProductJpaEntity postProductJpaEntity =
                                            postProductRepository
                                                    .findById(postProductImage.getPostProductId())
                                                    .orElseThrow(
                                                            () ->
                                                                    new CustomException(
                                                                            ErrorResponseCode
                                                                                    .POST_PRODUCT_NOT_FOUND));
                                    return postProductImageMapper.toJpaEntity(
                                            postProductImage, postProductJpaEntity);
                                })
                        .toList();

        return postProductImageRepository.saveAll(postProductImageJpaEntities).stream()
                .map(postProductImageMapper::toDomainEntity)
                .toList();
    }

    @Override
    public Optional<Post> loadPost(FindByIdQuery query) {
        return postRepository.findById(query.getId()).map(postMapper::toDomainEntity);
    }

    @Override
    public List<Post> loadPostsByDeleteOption(FindPostByDeleteOptionQuery query) {
        return postRepository.findAllByDeletedBefore(query).stream()
                .map(postMapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<Long> loadUserPickPopularPosts(FindUserPickPopularPostsQuery query) {
        return postRepository.findTopNPostsByViewCountAndLikeCount(
                query.getSince(), query.getLimit());
    }

    @Override
    public List<Long> loadUserPickBiblePosts(FindUserPickBiblePostsQuery query) {
        return postRepository.findTopNPostsByLikeCount(query.getLimit());
    }

    @Override
    public List<Long> loadTopPostsByBookmarkCount(FindTopPostsByBookmarkCountQuery query) {
        return postRepository.findTopNPostsByBookmarkCount(query.getLimit());
    }

    @Override
    public List<BookmarkYnWrapperVo> loadUserPickAllPostsByLatest(
            FindUserPickLatestPostsByCursorQuery query) {
        return postRepository.findPostsByLatestCursor(query).stream()
                .map(
                        e ->
                                new BookmarkYnWrapperVo(
                                        e.getBookmarkYn(),
                                        postMapper.toDomainEntity((PostJpaEntity) e.getData())))
                .toList();
    }

    @Override
    public List<PostIdAndBookmarkYnVo> loadPostIdAndBookmarkYn(FindByPostIdsAndUserQuery query) {
        return postRepository.findPostIdWithBookmarkYn(query);
    }

    @Override
    public List<PostWithUserAndBookmarkCountVo> loadPostWithUserAndBookmarkCount(
            FindByIdsQuery query) {
        return postRepository.findAllPostsWithUserAndBookmarkCount(query);
    }

    @Override
    public List<PostImage> loadPostImagesByPostId(FindByPostIdQuery query) {
        PostJpaEntity postJpaEntity =
                postRepository
                        .findById(query.getPostId())
                        .orElseThrow(() -> new CustomException(ErrorResponseCode.POST_NOT_FOUND));

        return postImageRepository.findAllByPost(postJpaEntity).stream()
                .map(postImageMapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<PostImage> loadPostImagesByPostIds(FindByIdsQuery query) {
        return postImageRepository.findAllByPostIdIn(query.getIds()).stream()
                .map(postImageMapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<PostProduct> loadPostProductsByPostId(FindByPostIdQuery query) {
        PostJpaEntity postJpaEntity =
                postRepository
                        .findById(query.getPostId())
                        .orElseThrow(() -> new CustomException(ErrorResponseCode.POST_NOT_FOUND));

        return postProductRepository.findAllByPost(postJpaEntity).stream()
                .map(postProductMapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<PostProduct> loadPostProductsByIds(FindByIdsQuery query) {
        return postProductRepository.findAllById(query.getIds()).stream()
                .map(postProductMapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<PostProduct> loadPostProductsByPostIds(FindByIdsQuery query) {
        return postProductRepository.findAllByPostIdIn(query.getIds()).stream()
                .map(postProductMapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<PostProductImage> loadPostProductImagesByPostProductIds(FindByIdsQuery query) {
        List<PostProductImageJpaEntity> postProductImageJpaEntities =
                postProductImageRepository.findAllByPostProductIdIn(query.getIds());

        return postProductImageJpaEntities.stream()
                .map(postProductImageMapper::toDomainEntity)
                .toList();
    }

    @Override
    public Optional<User> loadUserById(FindByIdQuery query) {
        return userRepository.findById(query.getId()).map(userMapper::toDomainEntity);
    }

    @Override
    public Optional<UserImage> loadUserImageByUserId(FindByUserIdQuery query) {
        UserJpaEntity userJpaEntity =
                userRepository
                        .findById(query.getUserId())
                        .orElseThrow(() -> CustomException.USER_NOT_FOUND);

        return userImageRepository
                .findByUserId(query.getUserId())
                .map(userImageMapper::toDomainEntity);
    }

    @Override
    public void updatePost(Post post) {
        PostJpaEntity postJpaEntity =
                postRepository
                        .findById(post.getId())
                        .orElseThrow(() -> new CustomException(ErrorResponseCode.POST_NOT_FOUND));

        postJpaEntity.updatePostForDomainEntity(post);
    }

    @Override
    public void updatePostProducts(List<PostProduct> postProducts) {
        List<Long> ids = postProducts.stream().map(PostProduct::getId).toList();
        Map<Long, PostProductJpaEntity> postProductJpaEntityMap =
                postProductRepository.findAllById(ids).stream()
                        .collect(
                                Collectors.toMap(PostProductJpaEntity::getId, Function.identity()));

        for (PostProduct postProduct : postProducts) {
            PostProductJpaEntity postProductJpaEntity =
                    postProductJpaEntityMap.get(postProduct.getId());
            postProductJpaEntity.updatePostProductForDomainEntity(postProduct);
        }
    }

    @Override
    public void updatePostProductImages(List<PostProductImage> postProductImages) {
        List<Long> ids = postProductImages.stream().map(PostProductImage::getId).toList();
        Map<Long, PostProductImageJpaEntity> postProductImageJpaEntityMap =
                postProductImageRepository.findAllById(ids).stream()
                        .collect(
                                Collectors.toMap(
                                        PostProductImageJpaEntity::getId, Function.identity()));

        for (PostProductImage postProductImage : postProductImages) {
            PostProductImageJpaEntity postProductImageJpaEntity =
                    postProductImageJpaEntityMap.get(postProductImage.getId());
            postProductImageJpaEntity.updatePostProductImageForDomainEntity(postProductImage);
        }
    }

    @Override
    public void deletePostsByIds(List<Long> postIds) {
        postRepository.deleteAllByIdInBatch(postIds);
    }

    @Override
    public void deletePostImages(List<PostImage> postImages) {
        List<Long> ids = postImages.stream().map(PostImage::getId).toList();
        postImageRepository.deleteAllByIdInBatch(ids);
    }

    @Override
    public void deletePostProducts(List<PostProduct> postProducts) {
        List<Long> ids = postProducts.stream().map(PostProduct::getId).toList();
        postProductRepository.deleteAllByIdInBatch(ids);
    }

    @Override
    public void deletePostProductsByIds(List<Long> postProductIds) {
        postProductRepository.deleteAllByIdInBatch(postProductIds);
    }

    @Override
    public void deletePostProductImages(List<PostProductImage> postProductImages) {
        List<Long> ids = postProductImages.stream().map(PostProductImage::getId).toList();
        postProductImageRepository.deleteAllByIdInBatch(ids);
    }

    @Override
    public List<PostWithBookmarkCountVo> loadAllPostsWithBookmarkCount(
            FindPostsByCreatedDateQuery query) {
        return postRepository.findAllPostsWithBookmarkCount(query);
    }

    @Override
    public List<UserWithPostCountVo> loadAllPostsWithUserAndBookmarkCount(
            FindPostsByCreatedDateQuery query) {
        return postRepository.findAllPostsWithUserAndBookmarkCount(query);
    }

    @Override
    public List<PostAndBookmarkCountVo> getPostAndBookmarkCount(
            FindBookmarkCountByPostIdsQuery query) {
        return postRepository.findBookmarkCountsByPostIds(query.getPostIds());
    }

    @Override
    public List<PostImage> loadRepresentativeImagesByPostIds(FindByIdsQuery query) {
        return postImageRepository.findRepresentativeImagesByPostIdIn(query).stream()
                .map(postImageMapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<Post> loadPostListByUsers(FindByUserIdsQuery query) {
        return postRepository.findAllByUserIdIn(query.getUserIds()).stream()
                .map(postMapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<UserIdAndNameVo> loadPostAndAuthorName(FindByIdsQuery query) {
        return userRepository.findUserNameByUserIds(query.getIds());
    }
}
