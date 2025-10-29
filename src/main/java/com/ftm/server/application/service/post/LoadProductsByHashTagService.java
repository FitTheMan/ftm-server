package com.ftm.server.application.service.post;

import com.ftm.server.application.port.in.post.LoadProductsByHashTagUseCase;
import com.ftm.server.application.port.out.persistence.post.LoadPostProductImagePort;
import com.ftm.server.application.port.out.persistence.post.LoadPostProductPort;
import com.ftm.server.application.port.out.persistence.post.LoadProductLikePort;
import com.ftm.server.application.query.FindByIdsQuery;
import com.ftm.server.application.query.FindByProductHashTagsQuery;
import com.ftm.server.application.vo.post.LoadProductAndUserLikeVo;
import com.ftm.server.application.vo.post.LoadProductsByHashTagVo;
import com.ftm.server.domain.entity.PostProduct;
import com.ftm.server.domain.entity.PostProductImage;
import com.ftm.server.domain.enums.ProductHashtag;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoadProductsByHashTagService implements LoadProductsByHashTagUseCase {

    private final LoadPostProductPort loadPostProductPort;
    private final LoadProductLikePort loadProductLikePort;
    private final LoadPostProductImagePort loadPostProductImagePort;

    @Override
    public List<LoadProductsByHashTagVo> execute(Long userId, List<ProductHashtag> hashtagList) {

        // 1. postProduct 해시태그 별 정보 조회
        List<PostProduct> postProducts = null;
        if (hashtagList == null || hashtagList.isEmpty()) {
            postProducts = loadPostProductPort.loadAllPostProduct();
        } else {
            postProducts =
                    loadPostProductPort.loadPostProductsByHashTags(
                            new FindByProductHashTagsQuery(hashtagList));
        }

        Map<Long, PostProduct> postProductMap =
                postProducts.stream()
                        .collect(
                                Collectors.toMap(
                                        PostProduct::getId, // key: productId
                                        Function.identity() // value: 해당 PostProduct 객체 자체
                                        ));

        // 2. 대상 product id 추출
        List<Long> productIds = postProducts.stream().map(PostProduct::getId).toList();

        // 3. 좋아요 여부 확인
        List<LoadProductAndUserLikeVo> loadProductAndUserLikeVos = null;
        if (userId == null) {
            loadProductAndUserLikeVos =
                    productIds.stream()
                            .map(p -> new LoadProductAndUserLikeVo(p, null, false))
                            .toList();
        } else {
            loadProductAndUserLikeVos =
                    loadProductLikePort.findProductLikeByUser(userId, productIds);
        }
        Map<Long, LoadProductAndUserLikeVo> productLikeMap =
                loadProductAndUserLikeVos.stream()
                        .collect(
                                Collectors.toMap(
                                        LoadProductAndUserLikeVo::getProductId, // key: productId
                                        Function.identity() // value: 해당 PostProduct 객체 자체
                                        ));

        // 4. 이미지 가져오기
        List<PostProductImage> postProductImages =
                loadPostProductImagePort.loadPostProductImagesByPostProductIds(
                        FindByIdsQuery.from(productIds));

        Map<Long, PostProductImage> productImageMap =
                postProductImages.stream()
                        .collect(
                                Collectors.toMap(
                                        PostProductImage::getPostProductId, // key: productId
                                        Function.identity() // value: 해당 PostProduct 객체 자체
                                        ));

        // 5. 결과값 merge
        return productIds.stream()
                .map(
                        p ->
                                LoadProductsByHashTagVo.from(
                                        postProductMap.get(p),
                                        productLikeMap.get(p),
                                        productImageMap.getOrDefault(p, null)))
                .toList();
    }
}
