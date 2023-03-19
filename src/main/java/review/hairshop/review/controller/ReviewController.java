package review.hairshop.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import review.hairshop.common.response.ApiResponse;
import review.hairshop.review.dto.*;
import review.hairshop.review.service.ReviewService;
import review.hairshop.review_image.service.ReviewImageService;

import javax.validation.Valid;
import java.util.List;

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
    @PostMapping("/new")
    public ApiResponse<ReviewResponseDto> postReview(@RequestAttribute Long memberId,@Valid ReviewNewReqDto reviewRequestDto){
        /**
         * paramDto로 다시 셋하는게 맞나? 효율적인가?
         * */
        ReviewNewParamDto reviewNewParamDto = ReviewNewParamDto.builder()
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

        Long reviewId = reviewService.createReview(memberId, reviewNewParamDto);

        return ApiResponse.success(ReviewResponseDto.builder().reviewId(reviewId).build());
    }

    /**
     * 해당 리뷰 상세 조회 -> 내가 쓴 리뷰와 다른 사람 리뷰 조회 구분 -> Reader.ME, Reader.OTHER -> 리뷰 삭제 활성화 비활성화
     **/
    @GetMapping("/{review_id}")
    public ApiResponse<ReviewAllInfoResDto> getReview(@RequestAttribute Long memberId,@PathVariable("review_id")Long reviewId){
        // 게시글 아이디 받아와서 정보 값들을 넘겨 받음
        ReviewAllInfoDto reviewAllInfoDto = reviewService.showReviewInfo(memberId,reviewId);
        return ApiResponse.success(ReviewAllInfoResDto.builder()
                        .memberName(reviewAllInfoDto.getMemberName())
                        .designer(reviewAllInfoDto.getDesigner())
                        .imageUrls(reviewAllInfoDto.getImageUrls())
                        .hairStyles(reviewAllInfoDto.getHairStyles())
                        .hairTypes(reviewAllInfoDto.getHairTypes())
                        .gender(reviewAllInfoDto.getGender())
                        .createAt(reviewAllInfoDto.getCreateAt())
                        .content(reviewAllInfoDto.getContent())
                        .date(reviewAllInfoDto.getDate())
                        .lengthStatus(reviewAllInfoDto.getLengthStatus())
                        .price(reviewAllInfoDto.getPrice())
                        .satisfaction(reviewAllInfoDto.getSatisfaction())
                        .reader(reviewAllInfoDto.getReader())
                        .shopName(reviewAllInfoDto.getShopName()).build());
    }

    /**
     * 내가 쓴 리뷰들 리스트 조회
     * ACTIVE한 것들만 가져오기
     **/
    @GetMapping("/my")
    public ApiResponse<ReviewMyListResDto> getMyReviewAll(@RequestAttribute Long memberId){
        // 게시글 아이디 받아와서 정보 값들을 넘겨 받음
        List<ReviewMyListInfoDto> myReviewInfoList = reviewService.getMyReviewInfoList(memberId);

        return ApiResponse.success(ReviewMyListResDto.builder().reviewMyListResDtos(myReviewInfoList).build());
    }


    /**
     * 리뷰 이미지 조회
     * ACTIVE한 것들만 가져오기
     **/
    @GetMapping("/image/{review_id}")
    public ApiResponse<ReviewImageResDto> getReviewImage(@PathVariable("review_id")Long reviewId){

        return ApiResponse.success(ReviewImageResDto.builder()
                .reviewImages(reviewImageService.getReviewImages(reviewId))
                .build());
    }


    /**
     * 자기가 쓴 리뷰만 삭제할 수 있음
     * */
    @PatchMapping("/withdraw/{review_id}")
    public ApiResponse<ReviewResponseMessageDto> patchReview(@RequestAttribute Long memberId, @PathVariable("review_id")Long reviewId){

        reviewService.withdrawReview(reviewId, memberId);
        return ApiResponse.success(ReviewResponseMessageDto.builder().resultMessage("리뷰가 삭제되었습니다.").build());
    }

}
