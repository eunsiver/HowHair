package review.hairshop.reveiwFacade.controller;

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


    //추후에 북마크 적용

    /**
     * 리뷰작성 -> 내가 쓴 리뷰 확인할 수 있도록 정보들 넘겨줌
     */
    /**
     * 이미지 수정
     * */
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

    /**
     * 해당 리뷰 상세 조회 -> 내가 쓴 리뷰와 다른 사람 리뷰 조회 구분 -> Reader.ME, Reader.OTHER -> 리뷰 삭제 활성화 비활성화
     **/
    @GetMapping("/review/{review_id}")
    public ApiResponse<ReviewDetailResponseDto> getReview(@RequestAttribute Long memberId, @PathVariable Long reviewId){

        return ApiResponse.success(reviewService.getReviewDetail(memberId,reviewId));
    }

    /**
     * 내가 쓴 리뷰들 리스트 조회
     * ACTIVE한 것들만 가져오기
     **/
    @GetMapping("/my/review/list")
    public ApiResponse<List<MyReviewListResponseDto>> getMyReviewList(@RequestAttribute Long memberId){
        // 게시글 아이디 받아와서 정보 값들을 넘겨 받음
        return ApiResponse.success(reviewService.getMyReviewList(memberId));
    }

    /**
     * 자기가 쓴 리뷰만 삭제할 수 있음
     * 이미지와 리뷰 모두 INACTIVE
     * */
    @PatchMapping("/review/withdraw/{review_id}")
    public ApiResponse<ReviewResponseMessageDto> patchReview(@RequestAttribute Long memberId, @PathVariable Long reviewId){
        reviewService.withdrawReview(reviewId,memberId);
        return ApiResponse.success(ReviewResponseMessageDto.builder().resultMessage("리뷰가 삭제되었습니다.").build());
    }
}
