package review.hairshop.reveiwFacade.review.repository;

import review.hairshop.reveiwFacade.dto.HairShopDto;
import review.hairshop.reveiwFacade.dto.responseDto.ReviewListResponseDto;

import java.util.List;

public interface ReviewRepositoryCustom {
    List<HairShopDto> OrderShopByReviewCount();
    List<ReviewListResponseDto> OrderReviewByBookmarkCount();
}
