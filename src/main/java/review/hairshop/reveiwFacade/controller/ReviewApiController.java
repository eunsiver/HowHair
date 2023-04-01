package review.hairshop.reveiwFacade.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import review.hairshop.common.response.ApiResponse;
import review.hairshop.reveiwFacade.dto.requestDto.ReviewReqestDto;
import review.hairshop.reveiwFacade.dto.ReviewParamDto;
import review.hairshop.reveiwFacade.dto.responseDto.MyReviewListResponseDto;
import review.hairshop.reveiwFacade.dto.responseDto.ReviewDetailResponseDto;
import review.hairshop.reveiwFacade.dto.responseDto.ReviewResponseMessageDto;
import review.hairshop.reveiwFacade.service.ReviewService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ReviewApiController {
    private final ReviewService reviewService;

    @PostMapping("/review/new")
    public ApiResponse<ReviewDetailResponseDto> postReview(@RequestAttribute Long memberId, @Validated ReviewReqestDto reviewRequestDto){

        ReviewParamDto reviewParamDto = ReviewParamDto.builder()
                .content(reviewRequestDto.getContent())
                .date(reviewRequestDto.getDate())
                .shopName(reviewRequestDto.getShopName())
                .lengthStatus(reviewRequestDto.getLengthStatus())
                .designerName(reviewRequestDto.getDesignerName())
                .price(reviewRequestDto.getPrice())
                .satisfaction(reviewRequestDto.getSatisfaction())
                .dyeing(reviewRequestDto.getDyeing())
                .hairCut(reviewRequestDto.getHairCut())
                .perm(reviewRequestDto.getPerm())
                .straightening(reviewRequestDto.getStraightening())
                .imageFiles(reviewRequestDto.getImageFiles())
                .build();

        return ApiResponse.success(reviewService.registerReview(memberId, reviewParamDto));
    }

    @GetMapping("/review/{reviewId}")
    public ApiResponse<ReviewDetailResponseDto> getReview(@RequestAttribute Long memberId, @PathVariable Long reviewId){

        return ApiResponse.success(reviewService.getReviewDetail(memberId,reviewId));
    }

    @GetMapping("/my/review/list")
    public ApiResponse<List<MyReviewListResponseDto>> getMyReviewList(@RequestAttribute Long memberId){

        return ApiResponse.success(reviewService.getMyReviewList(memberId));
    }

    @PatchMapping("/review/withdraw/{reviewId}")
    public ApiResponse<ReviewResponseMessageDto> patchReview(@RequestAttribute Long memberId, @PathVariable Long reviewId){

        reviewService.withdrawReview(reviewId,memberId);
        return ApiResponse.success(ReviewResponseMessageDto.builder().resultMessage("리뷰가 삭제되었습니다.").build());
    }
}
