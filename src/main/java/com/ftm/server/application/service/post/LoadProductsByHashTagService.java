package com.ftm.server.application.service.post;

import com.ftm.server.application.port.in.post.LoadProductsByHashTagUseCase;
import com.ftm.server.application.port.out.cache.LoadProductsByPopularityCachePort;
import com.ftm.server.application.port.out.persistence.post.LoadPostProductImagePort;
import com.ftm.server.application.port.out.persistence.post.LoadPostProductPort;
import com.ftm.server.application.port.out.persistence.post.LoadProductLikePort;
import com.ftm.server.application.query.FindByIdsQuery;
import com.ftm.server.application.query.FindByProductHashTagsQuery;
import com.ftm.server.application.vo.post.LoadProductAndUserLikeVo;
import com.ftm.server.application.vo.post.LoadProductsByHashTagVo;
import com.ftm.server.application.vo.post.LoadProductsByHashTagVoWrapper;
import com.ftm.server.application.vo.post.ProductIdAndScoreVo;
import com.ftm.server.domain.entity.PostProduct;
import com.ftm.server.domain.entity.PostProductImage;
import com.ftm.server.domain.enums.ProductHashtag;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    private final LoadProductsByPopularityCachePort loadProductsByPopularityCachePort;

    @Override
    public LoadProductsByHashTagVoWrapper execute(
            Long userId, List<ProductHashtag> hashtagList, Integer limit, Double score) {

        // 0. 인기순으로 정렬된 product id를 cache 에서 조회
        List<ProductIdAndScoreVo> productIdAndScoreList =
                loadProductsByPopularityCachePort.loadProductsByPopularity();
        if (score != null) {
            productIdAndScoreList =
                    productIdAndScoreList.stream().filter(a -> a.getScore() < score).toList();
        }

        if (productIdAndScoreList.isEmpty()) {
            return LoadProductsByHashTagVoWrapper.of(List.of(), false, 0.0);
        }

        // 1. postProduct 해시태그 별 정보 조회
        List<PostProduct> postProducts;
        if (hashtagList == null || hashtagList.isEmpty()) {
            postProducts = loadPostProductPort.loadAllPostProduct();
        } else {
            postProducts =
                    loadPostProductPort.loadPostProductsByHashTags(
                            new FindByProductHashTagsQuery(hashtagList));
        }

        if (postProducts.isEmpty()) {
            return LoadProductsByHashTagVoWrapper.of(List.of(), false, 0.0);
        }

        Map<Long, PostProduct> postProductMap =
                postProducts.stream()
                        .collect(
                                Collectors.toMap(
                                        PostProduct::getId, // key: productId
                                        Function.identity() // value: 해당 PostProduct 객체 자체
                                        ));

        // 2. 대상 product id 추출 + score 순서대로 limit 만큼 커팅
        int beforeSize = productIdAndScoreList.size();
        Set<Long> tempProductIds =
                postProducts.stream().map(PostProduct::getId).collect(Collectors.toSet());

        // 캐시에 존재하는 상품 중(인기순으로 정렬되어 있음), 해시태그와 관련 있는 상품만 조회 + limit 만큼 자르기
        List<ProductIdAndScoreVo> productIdAndScoreVoList =
                productIdAndScoreList.stream()
                        .filter(vo -> tempProductIds.contains(vo.getProductId())) // DB에 존재하는 상품만
                        .limit(limit) // limit 만큼 자르기
                        .toList();
        int finalSize = productIdAndScoreVoList.size();
        Double lastScore =
                productIdAndScoreVoList.get(productIdAndScoreVoList.size() - 1).getScore();
        List<Long> productIds =
                productIdAndScoreVoList.stream().map(ProductIdAndScoreVo::getProductId).toList();

        // 3. 유저 좋아요 여부 확인
        List<LoadProductAndUserLikeVo> loadProductAndUserLikeVos = null;
        if (userId == null) {
            loadProductAndUserLikeVos =
                    productIds.stream()
                            .map(p -> new LoadProductAndUserLikeVo(p, null, false))
                            .toList();
        } else {
            loadProductAndUserLikeVos =
                    loadProductLikePort.findProductLikeByUser(userId, productIds);
            System.out.println("!!!!!!!!!!!!!!\n\n\\n");
            System.out.println(loadProductAndUserLikeVos.size());
            System.out.println(loadProductAndUserLikeVos.get(0));
        }
        Map<Long, LoadProductAndUserLikeVo> productLikeMap =
                loadProductAndUserLikeVos.stream()
                        .collect(
                                Collectors.toMap(
                                        LoadProductAndUserLikeVo::getProductId, // key:
                                        // productId
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
        List<LoadProductsByHashTagVo> tempResult =
                productIds.stream()
                        .map(
                                p ->
                                        LoadProductsByHashTagVo.from(
                                                postProductMap.get(p),
                                                productLikeMap.get(p),
                                                productImageMap.getOrDefault(p, null)))
                        .toList();

        return LoadProductsByHashTagVoWrapper.of(tempResult, beforeSize > finalSize, lastScore);
    }
}
