package review.hairshop.reveiwFacade.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import review.hairshop.common.response.ApiResponse;
import review.hairshop.reveiwFacade.dto.requestDto.ReviewReqestDto;
import review.hairshop.reveiwFacade.dto.responseDto.ReviewParamDto;
import review.hairshop.reveiwFacade.dto.responseDto.ReviewResponseDto;
import review.hairshop.reveiwFacade.dto.responseDto.ReviewMyListRespnseDto;
import review.hairshop.reveiwFacade.dto.responseDto.ReviewResponseMessageDto;
import review.hairshop.reveiwFacade.review.dto.*;
import review.hairshop.reveiwFacade.service.ReviewService;
import review.hairshop.reveiwFacade.review_image.service.ReviewImageService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewImageService reviewImageService;


    //추후에 북마크 적용

    /**
     * 리뷰작성 -> 내가 쓴 리뷰 확인할 수 있도록 정보들 넘겨줌
     */
    /**
     * 이미지 수정
     * */
    @PostMapping("/new")
    public ApiResponse<ReviewResponseDto> postReview(@RequestAttribute Long memberId, @Validated @RequestBody ReviewReqestDto reviewRequestDto){

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
    @GetMapping("/{review_id}")
    public ApiResponse<ReviewResponseDto> getReview(@RequestAttribute Long memberId, @PathVariable("review_id")Long reviewId){
        // 게시글 아이디 받아와서 정보 값들을 넘겨 받음
        ReviewAllInfoDto reviewAllInfoDto = reviewService.showReviewInfo(memberId,reviewId);

        return ApiResponse.success(ReviewResponseDto.builder()
                        .memberName(reviewAllInfoDto.getMemberName())
                        .designer(reviewAllInfoDto.getDesigner())
                        .imageUrls(reviewAllInfoDto.getImageUrls())
                        .hairCut(reviewAllInfoDto.getHairCut())
                        .dyeing(reviewAllInfoDto.getDyeing())
                        .perm(reviewAllInfoDto.getPerm())
                        .gender(reviewAllInfoDto.getGender())
                        .createAt(reviewAllInfoDto.getCreateAt())
                        .content(reviewAllInfoDto.getContent())
                        .date(reviewAllInfoDto.getDate())
                        .lengthStatus(reviewAllInfoDto.getLengthStatus())
                        .price(reviewAllInfoDto.getPrice())
                        .satisfaction(reviewAllInfoDto.getSatisfaction())
                        .isReaderSameWriter(reviewAllInfoDto.getIsReaderSameWriter())
                        .shopName(reviewAllInfoDto.getShopName()).build());
    }

    /**
     * 내가 쓴 리뷰들 리스트 조회
     * ACTIVE한 것들만 가져오기
     **/
    @GetMapping("/my")
    public ApiResponse<ReviewMyListRespnseDto> getMyReviewAll(@RequestAttribute Long memberId){
        // 게시글 아이디 받아와서 정보 값들을 넘겨 받음
        return ApiResponse.success(ReviewMyListRespnseDto.builder().reviewMyListResDtos(reviewService.getMyReviewInfoList(memberId)).build());
    }


    /**
     * 리뷰 이미지 조회
     * ACTIVE한 것들만 가져오기
     * 리뷰 이미지 path로 조회할 수 있도록 다시 수정
     **/
//    @GetMapping("/image/{review_id}")
//    public ApiResponse<ReviewImageResDto> getReviewImage(@PathVariable("review_id")Long reviewId){
//
//        return ApiResponse.success(ReviewImageResDto.builder()
//                .reviewImages(reviewImageService.getReviewImages(reviewId))
//                .build());
//    }


    /**
     * 자기가 쓴 리뷰만 삭제할 수 있음
     * 이미지와 리뷰 모두 INACTIVE
     * */
    @PatchMapping("/withdraw/{review_id}")
    public ApiResponse<ReviewResponseMessageDto> patchReview(@RequestAttribute Long memberId, @PathVariable("review_id")Long reviewId){

        reviewService.withdrawReview(reviewId, memberId);
        return ApiResponse.success(ReviewResponseMessageDto.builder().resultMessage("리뷰가 삭제되었습니다.").build());
    }

}
