package review.hairshop.reveiwFacade.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import review.hairshop.common.response.ApiResponse;
import review.hairshop.reveiwFacade.dto.HairSearchDto.ReviewSearchCondition;
import review.hairshop.reveiwFacade.dto.Result;
import review.hairshop.reveiwFacade.dto.requestDto.ReviewReqestDto;
import review.hairshop.reveiwFacade.dto.ReviewParamDto;
import review.hairshop.reveiwFacade.dto.responseDto.ReviewDetailResponseDto;
import review.hairshop.reveiwFacade.dto.responseDto.ReviewResponseMessageDto;
import review.hairshop.reveiwFacade.service.ReviewService;

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
    public ApiResponse<Result> getMyReviewList(@RequestAttribute Long memberId){

        return ApiResponse.success(new Result(reviewService.getMyReviewList(memberId)));
    }

    @PatchMapping("/review/withdraw/{reviewId}")
    public ApiResponse<ReviewResponseMessageDto> patchReview(@RequestAttribute Long memberId, @PathVariable Long reviewId){

        reviewService.withdrawReview(reviewId,memberId);
        return ApiResponse.success(ReviewResponseMessageDto.builder().resultMessage("리뷰가 삭제되었습니다.").build());
    }

    /************************************************************************************************************************************************/

    @GetMapping("/review/list/cut")
    public ApiResponse<Result> getHairCutReviewList(@RequestAttribute Long memberId, ReviewSearchCondition condition){

        return ApiResponse.success(new Result(reviewService.getSearchReviewList(memberId,condition)));
    }
    @GetMapping("/review/list/perm")
    public ApiResponse<Result> getPermReviewList(@RequestAttribute Long memberId, ReviewSearchCondition condition){

        return ApiResponse.success(new Result(reviewService.getSearchReviewList(memberId, condition)));
    }
    @GetMapping("/review/list/straightening")
    public ApiResponse<Result> getStraighteningReviewList(@RequestAttribute Long memberId, ReviewSearchCondition condition){

        return ApiResponse.success(new Result(reviewService.getSearchReviewList(memberId, condition)));
    }
    @GetMapping("/review/list/dyeing")
    public ApiResponse<Result> getDyeingReviewList(@RequestAttribute Long memberId, ReviewSearchCondition condition){

        return ApiResponse.success(new Result(reviewService.getSearchReviewList(memberId,condition)));
    }


    /************************************************************************************************************************************************/

    @GetMapping("/recommend/mytype/list")
    public ApiResponse<Result> getMyTypeReviewList(@RequestAttribute Long memberId){

        return ApiResponse.success(new Result(reviewService.getMyType_recommandReviewList(memberId)));
    }

   // 북마크가 가장 많은 인기 많은 스타일(Review 리스트) 조회
    @GetMapping("/recommend/bookmark/list")
    public ApiResponse<Result> getPopularReviewList(@RequestAttribute Long memberId){

        return ApiResponse.success(new Result(reviewService.getPopularBookmarkList(memberId)));
    }

    /************************************************************************************************************************************************/
    //미용실 검색 화면
    @GetMapping("/hairshop/review/list")
    public ApiResponse<Result> getHairShopReviewList(@RequestAttribute Long memberId, @RequestParam String shopName){

        return ApiResponse.success(new Result(reviewService.getHairShopReviewList(memberId,shopName)));
    }
    /************************************************************************************************************************************************/
     //메인 화면
    @GetMapping("/main/mytype/list")
    public ApiResponse<Result> getMain_MyTypeReviewList(@RequestAttribute Long memberId){

        return ApiResponse.success(new Result(reviewService.getMainMyTypeReviewList(memberId)));
    }
    @GetMapping("/main/bookmark/list")
    public ApiResponse<Result> getMain_BookmarkReviewList(){

        return ApiResponse.success(new Result(reviewService.getMainBookmarkReviewList()));
    }
}
