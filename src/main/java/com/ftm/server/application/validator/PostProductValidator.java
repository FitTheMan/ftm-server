package com.ftm.server.application.validator;

import com.ftm.server.application.command.post.HasImageIndex;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class PostProductValidator {

    // 이미지 범위 검증
    public static void validateImageIndexRange(
            List<? extends HasImageIndex> data, List<MultipartFile> imageFiles) {
        int maxIndex = imageFiles.size();

        for (int imageIndex : getValidImageIndexes(data)) {
            System.out.println(imageIndex);
            if (imageIndex <= 0 || imageIndex > maxIndex) {
                log.warn("imageIndex 범위 검증 실패: index={}, maxIndex={}", imageIndex, maxIndex);
                throw new CustomException(ErrorResponseCode.INVALID_POST_PRODUCT_IMAGE_MAPPING);
            }
        }
    }

    // 중복된 이미지 인덱스 검증
    public static void validateImageIndexDuplication(List<? extends HasImageIndex> data) {
        Set<Integer> seen = new HashSet<>();

        for (int imageIndex : getValidImageIndexes(data)) {
            if (!seen.add(imageIndex)) {
                log.warn("중복된 imageIndex: index={}", imageIndex);
                throw new CustomException(ErrorResponseCode.INVALID_POST_PRODUCT_IMAGE_MAPPING);
            }
        }
    }

    // 이미지를 등록하지 않은 상품을 제외한 상품들의 imageIndex 목록 조회
    private static List<Integer> getValidImageIndexes(List<? extends HasImageIndex> data) {
        return data.stream()
                .map(HasImageIndex::getImageIndex)
                .filter(index -> index != -1)
                .toList();
    }

    // 상품 : 이미지 매핑 검증
    public static void validateOneToOneImageProductMapping(
            List<? extends HasImageIndex> data, List<MultipartFile> imageFiles) {
        long imageCount = imageFiles.size();
        long productCount =
                data.stream() // 이미지를 등록하지 않은 상품을 제외한 수
                        .filter(product -> product.getImageIndex() != -1)
                        .count();

        if (imageCount != productCount) {
            log.warn("이미지 매핑 불일치: 이미지를 등록한 데이터 개수={}, 업로드된 이미지 수={}", productCount, imageCount);
            throw new CustomException(ErrorResponseCode.INVALID_POST_PRODUCT_IMAGE_MAPPING);
        }
    }
}
