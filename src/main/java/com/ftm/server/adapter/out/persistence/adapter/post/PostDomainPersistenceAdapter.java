package com.ftm.server.adapter.out.persistence.adapter.post;

import com.ftm.server.adapter.out.persistence.mapper.*;
import com.ftm.server.adapter.out.persistence.model.*;
import com.ftm.server.adapter.out.persistence.repository.*;
import com.ftm.server.application.port.out.persistence.post.*;
import com.ftm.server.application.query.FindByIdQuery;
import com.ftm.server.application.query.FindByPostIdQuery;
import com.ftm.server.application.query.FindByPostProductIdsQuery;
import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.common.annotation.Adapter;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
                UpdatePostPort {

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
    public Map<Long, PostProductImage> loadPostProductImagesByPostProductIds(
            FindByPostProductIdsQuery query) {
        List<PostProductImageJpaEntity> postProductImageJpaEntities =
                postProductImageRepository.findByPostProductIds(query);

        return postProductImageJpaEntities.stream()
                .map(postProductImageMapper::toDomainEntity)
                .collect(Collectors.toMap(PostProductImage::getPostProductId, Function.identity()));
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
}
