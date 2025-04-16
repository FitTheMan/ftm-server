package com.ftm.server.adapter.out.persistence.adapter.post;

import com.ftm.server.adapter.out.persistence.mapper.PostImageMapper;
import com.ftm.server.adapter.out.persistence.mapper.PostMapper;
import com.ftm.server.adapter.out.persistence.mapper.PostProductImageMapper;
import com.ftm.server.adapter.out.persistence.mapper.PostProductMapper;
import com.ftm.server.adapter.out.persistence.model.*;
import com.ftm.server.adapter.out.persistence.repository.*;
import com.ftm.server.application.port.out.persistence.post.SavePostImagePort;
import com.ftm.server.application.port.out.persistence.post.SavePostPort;
import com.ftm.server.application.port.out.persistence.post.SavePostProductImagePort;
import com.ftm.server.application.port.out.persistence.post.SavePostProductPort;
import com.ftm.server.common.annotation.Adapter;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.Post;
import com.ftm.server.domain.entity.PostImage;
import com.ftm.server.domain.entity.PostProduct;
import com.ftm.server.domain.entity.PostProductImage;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class PostDomainPersistenceAdapter
        implements SavePostPort, SavePostImagePort, SavePostProductPort, SavePostProductImagePort {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final PostProductRepository postProductRepository;
    private final PostProductImageRepository postProductImageRepository;
    private final UserRepository userRepository;

    private final PostMapper postMapper;
    private final PostImageMapper postImageMapper;
    private final PostProductMapper postProductMapper;
    private final PostProductImageMapper postProductImageMapper;

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
}
