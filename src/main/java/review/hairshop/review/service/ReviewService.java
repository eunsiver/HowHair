package review.hairshop.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import review.hairshop.common.enums.*;
import review.hairshop.common.response.ApiException;
import review.hairshop.common.response.ApiResponseStatus;
import review.hairshop.member.Member;
import review.hairshop.member.repository.MemberRepository;
import review.hairshop.review.Review;
import review.hairshop.review.dto.ReviewAllInfoDto;
import review.hairshop.review.dto.ReviewMyListInfoDto;
import review.hairshop.review.dto.ReviewNewParamDto;
import review.hairshop.review.repository.ReviewRepository;
import review.hairshop.review_image.ReviewImage;
import review.hairshop.review_image.service.AwsS3Service;
import review.hairshop.review_image.service.ReviewImageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static review.hairshop.common.enums.Status.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final AwsS3Service awsS3Service;
    private final ReviewImageService reviewImageService;

    private Member getMember(Long memberId){
        Member findMember = memberRepository.findByIdAndStatus(memberId, ACTIVE).orElseThrow(
                () -> {
                    throw new ApiException(ApiResponseStatus.INVALID_MEMBER, "유효하지 않은 Member Id로 Member를 조회하려고 했습니다.");
                }
        );

        return findMember;
    }
    private Review getReview(Long reviewId){
        Review findReview=reviewRepository.findByIdAndStatus(reviewId,ACTIVE).orElseThrow(
                () -> {
                    throw new ApiException(ApiResponseStatus.INVALID_REVIEW, "존재하지 않는 리뷰입니다.");
                }
        );
        return findReview;
    }


    //리뷰에다 저장
    @Transactional
    public Long createReview(Long memberId, ReviewNewParamDto reviewNewParamDto) {
        //status 고려
        Member member=getMember(memberId);

        /**
         * cut, perm, dyeing, straightening이 DTO로 들어올때부터 기본이 NONE으로 받는다고 가정
         * */
        Review review = Review.builder()
                .content(reviewNewParamDto.getContent())
                .date(reviewNewParamDto.getDate())
                .status(ACTIVE)// 삭제하면 Status.INACTIVE로 변경
                .designerName(reviewNewParamDto.getDesignerName())
                .satisfaction(reviewNewParamDto.getSatisfaction())
                .hairShopName(reviewNewParamDto.getShopName())
                .price(reviewNewParamDto.getPrice())
                .content(reviewNewParamDto.getContent())
                .hairCut(reviewNewParamDto.getHairCut())
                .dyeing(reviewNewParamDto.getDyeing())
                .straightening(reviewNewParamDto.getStraightening())
                .perm(reviewNewParamDto.getPerm())
                .lengthStatus(reviewNewParamDto.getLengthStatus())
                .member(member)
                .build();

        reviewRepository.save(review);

        /**
         * 리뷰에서 이미지를 받았다면
         * **/
        if(reviewNewParamDto.getImageFiles()!=null){
            List<String> imgPaths = awsS3Service.upload(reviewNewParamDto.getImageFiles());
            for (String imgUrl : imgPaths) {
                ReviewImage img = ReviewImage.builder()
                        .url(imgUrl)
                        .status(ACTIVE)
                        .review(review)
                        .build();
                reviewImageService.saveImage(img);
            }
        }
        return review.getId();
    }

    /**
     * 리뷰 삭제
     * delete하는 것이 아니라 Status를 active->inactive로 변경
     * 이미지들도 inactive로 변경
     *
     * 프론트에서 게시글 번호를 전달 받음
     * 리뷰를 작성한 자와 현재 로그인한 자가 같은지 확인하여 INACTIVE
     *
     * 해당 유저가 리뷰이미지를 하나도 안 올릴 수 있음을 유의!
     **/
    @Transactional
    public void withdrawReview(Long reviewId, Long memberId) {
        //리뷰를 가지고 옴
        Review review=getReview(reviewId);
        Member member=getMember(memberId);

        if(review.getMember().getId() == member.getId()){

            reviewImageService.changeImageInactive(review);
            review.changeStatus(INACTIVE);
        }
    }


    //페이징 처리가 안되는 문제
    public List<Review> getMyReviewList(Long memberId) {
        return reviewRepository.findAllByMemberIdAndStatus(memberId,ACTIVE);
    }
    public List<ReviewMyListInfoDto> getMyReviewInfoList(Long memberId){

        List<Review> myReviewList = getMyReviewList(memberId);
        List<ReviewMyListInfoDto> reviewMyListInfoDtosList =new ArrayList<>();


        for(Review review:myReviewList){

            Map<String,List<String>> hairMap = getHairStylesAndTypes(review);

            ReviewMyListInfoDto reviewMyListInfoDto = ReviewMyListInfoDto.builder()
                    .shopName(review.getHairShopName())
                    .price(review.getPrice())
                    .reviewId(review.getId())
                    .hairTypes(hairMap.get("hairStyles"))
                    .hairStyles(hairMap.get("hairTypes"))
                    .build();

            reviewMyListInfoDtosList.add(reviewMyListInfoDto);
        }
        return reviewMyListInfoDtosList;
    }

    public ReviewAllInfoDto showReviewInfo(Long loginedMember,Long reviewId) {

        Reader checkReader=Reader.OTHER;

        Review review=getReview(reviewId);
        Member member=getMember(review.getMember().getId());
        List<String> reviewImageList=reviewImageService.getReviewImages(reviewId);

        Map<String,List<String>> hairMap=getHairStylesAndTypes(review);

        if(review.getMember().getId()==loginedMember){
            checkReader=Reader.ME;
        }

        return ReviewAllInfoDto.builder()
                .createAt(review.getCreatedAt())
                .content(review.getContent())
                .date(review.getDate())
                .designer(review.getDesignerName())
                .hairStyles(hairMap.get("hairStyles"))
                .hairTypes(hairMap.get("hairTypes"))
                .shopName(review.getHairShopName())
                .lengthStatus(review.getLengthStatus())
                .price(review.getPrice())
                .satisfaction(review.getSatisfaction())
                .memberName(member.getName())
                .gender(member.getGender())
                .imageUrls(reviewImageList)
                .reader(checkReader)
                .build();
    }

    public Map<String,List<String>> getHairStylesAndTypes(Review review){

        Map<String,List<String>> hairMap=new ConcurrentHashMap<>();
        List<String> hairStyles= new ArrayList<>();
        List<String> hairTypes=new ArrayList<>();

        if(review.getDyeing()!= Dyeing.NONE){
            hairStyles.add(String.valueOf(review.getDyeing()));
            hairTypes.add("염색");
        }
        if(review.getPerm()!= Perm.NONE){
            hairStyles.add(String.valueOf(review.getPerm()));
            hairTypes.add("펌");
        }
        if(review.getStraightening()!= Straightening.NONE){
            hairStyles.add(String.valueOf(review.getStraightening()));
            hairTypes.add("매직");
        }
        if(review.getHairCut()!= Hair_Cut.NONE){
            hairStyles.add(String.valueOf(review.getHairCut()));
            hairTypes.add("커트");
        }

        hairMap.put("hairStyles",hairStyles);
        hairMap.put("hairTypes",hairTypes);

        return hairMap;
    }
}
