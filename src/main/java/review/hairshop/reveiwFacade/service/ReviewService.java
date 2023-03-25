package review.hairshop.reveiwFacade.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import review.hairshop.common.response.ApiException;
import review.hairshop.common.response.ApiResponseStatus;
import review.hairshop.common.utils.FilesUtil;
import review.hairshop.member.Member;
import review.hairshop.member.repository.MemberRepository;
import review.hairshop.reveiwFacade.dto.responseDto.ReviewResponseDto;
import review.hairshop.reveiwFacade.review_image.ReviewImage;
import review.hairshop.reveiwFacade.review_image.repository.ReviewImageRepository;
import review.hairshop.reveiwFacade.review.Review;
import review.hairshop.reveiwFacade.dto.responseDto.ReviewParamDto;
import review.hairshop.reveiwFacade.review.repository.ReviewRepository;
import java.util.List;
import java.util.stream.Collectors;

import static review.hairshop.common.enums.Status.*;
import static review.hairshop.common.enums.isReaderSameWriter.DIFF;
import static review.hairshop.common.enums.isReaderSameWriter.SAME;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final FilesUtil filesUtil;
    private final ReviewImageRepository reviewImageRepository;


    //리뷰에다 저장
    @Transactional
    public ReviewResponseDto registerReview(Long memberId, ReviewParamDto reviewParamDto) {

        Member member = getMember(memberId);

        Review review = createReview(reviewParamDto, member);
        reviewRepository.save(review);

        /**
         * 이미지 형식이 올바른지 확인
         * */
        if (!filesUtil.isCorrectFormImage(reviewParamDto.getImageFiles()))
            throw new ApiException(ApiResponseStatus.WRONG_IMAGE, "이미지 형식이 올바르지 않습니다");

        /**
         * 리뷰 이미지가 없다면 디폴트 이미지 넣어주고 바로 reponse로 넘겨주기
         * Entity들을 먼저 save한 후 파일들을 저장
         * **/
        if (reviewParamDto.getImageFiles().isEmpty()) {
            List<String> sampleImagePath = filesUtil.getSampleUrlList();
            return createReveiewResponse(member, review, sampleImagePath);
        }

        /** 2_2.그렇지 않고 함께 등록할 이미지가 하나 이상 존재하면   <항상 DB먼저 수행 후 - File 작업을 수행해야 함>
         * -> 각 이미지들을 저장할 경로를 가진 ReviewImage 엔티티들을 DB에 save한 후 -> 실제 그 경로에 각 사진을 저장한다. */
        List<String> createdImgPath = filesUtil.createImagePath(review.getId(), reviewParamDto.getImageFiles());
        List<ReviewImage> reviewImageList = createdImgPath.stream()
                .map(i -> ReviewImage.builder().review(review).status(ACTIVE).url(i).build())
                .collect(Collectors.toList());
        /**
         * 이미지 엔티티에 저장
         * 각각 save하는 것보다, saveAll 하는게 트랜잭션 상 성능이 더 좋음
         * */
        reviewImageRepository.saveAll(reviewImageList);

        /**
         * s3에 저장
         * */
        filesUtil.putImageInS3(createdImgPath, reviewParamDto.getImageFiles());
        return createReveiewResponse(member, review, createdImgPath);
    }


    /**
     * 리뷰 삭제
     * delete하는 것이 아니라 Status를 active->inactive로 변경
     * 이미지들도 inactive로 변경
     * <p>
     * 프론트에서 게시글 번호를 전달 받음
     * 리뷰를 작성한 자와 현재 로그인한 자가 같은지 확인하여 INACTIVE
     * <p>
     * 해당 유저가 리뷰이미지를 하나도 안 올릴 수 있음을 유의!
     **/
//    @Transactional
//    public void withdrawReview(Long reviewId, Long memberId) {
//        //리뷰를 가지고 옴
//        Review review=getReview(reviewId);
//        Member member=getMember(memberId);
//
//        if(review.getMember().getId() == member.getId()){
//
//            reviewImageService.changeImageInactive(review);
//            review.changeStatus(INACTIVE);
//        }
//    }
//
//    public List<Review> getMyReviewList(Long memberId) {
//        return reviewRepository.findAllByMemberIdAndStatus(memberId,ACTIVE);
//    }
//    public List<ReviewMyListInfoDto> getMyReviewInfoList(Long memberId){
//
//        List<Review> myReviewList = getMyReviewList(memberId);
//        List<ReviewMyListInfoDto> reviewMyListInfoDtosList =new ArrayList<>();
//
//
//        for(Review review:myReviewList){
//
//
//            ReviewMyListInfoDto reviewMyListInfoDto = ReviewMyListInfoDto.builder()
//                    .shopName(review.getHairShopName())
//                    .price(review.getPrice())
//                    .reviewId(review.getId())
//                    .straightening(review.getStraightening())
//                    .dyeing(review.getDyeing())
//                    .hairCut(review.getHairCut())
//                    .perm(review.getPerm())
//                    .build();
//
//            reviewMyListInfoDtosList.add(reviewMyListInfoDto);
//        }
//        return reviewMyListInfoDtosList;
//    }
//
//    public ReviewAllInfoDto showReviewInfo(Long loginedMember,Long reviewId) {
//
//        isReaderSameWriter checkIsReaderSameWriter = DIFF;
//
//        Review review=getReview(reviewId);
//        Member member=getMember(review.getMember().getId());
//
//        List<String> reviewImageList=reviewImageService.getReviewImages(reviewId);
//
//
//        if(review.getMember().getId()==loginedMember){
//            checkIsReaderSameWriter = SAME;
//        }
//
//        return ReviewAllInfoDto.builder()
//                .createAt(review.getCreatedAt())
//                .content(review.getContent())
//                .date(review.getDate())
//                .designer(review.getDesignerName())
//                .dyeing(review.getDyeing())
//                .hairCut(review.getHairCut())
//                .perm(review.getPerm())
//                .shopName(review.getHairShopName())
//                .lengthStatus(review.getLengthStatus())
//                .price(review.getPrice())
//                .satisfaction(review.getSatisfaction())
//                .memberName(member.getName())
//                .gender(member.getGender())
//                .imageUrls(reviewImageList)
//                .isReaderSameWriter(checkIsReaderSameWriter)
//                .build();
//    }
    public ReviewResponseDto createReveiewResponse(Member member, Review review, List<String> imageUrl) {
        return ReviewResponseDto.builder()
                .isReaderSameWriter(member.getId().equals(review.getMember().getId()) ? SAME : DIFF)
                .shopName(review.getHairShopName())
                .imageUrls(imageUrl)
                .satisfaction(review.getSatisfaction())
                .memberName(member.getName())
                .gender(member.getGender())
                .date(review.getDate())
                .designer(review.getDesignerName())
                .createAt(review.getCreatedAt())
                .hairCut(review.getHairCut())
                .perm(review.getPerm())
                .dyeing(review.getDyeing())
                .lengthStatus(review.getLengthStatus())
                .price(review.getPrice())
                .content(review.getContent())
                .build();
    }

    /**
     * cut, perm, dyeing, straightening이 DTO로 들어올때부터 기본이 NONE으로 받는다고 가정
     */
    private Review createReview(ReviewParamDto reviewParamDto, Member member) {
        return Review.builder()
                .content(reviewParamDto.getContent())
                .date(reviewParamDto.getDate())
                .status(ACTIVE)// 삭제하면 Status.INACTIVE로 변경
                .designerName(reviewParamDto.getDesignerName())
                .satisfaction(reviewParamDto.getSatisfaction())
                .hairShopName(reviewParamDto.getShopName())
                .price(reviewParamDto.getPrice())
                .content(reviewParamDto.getContent())
                .hairCut(reviewParamDto.getHairCut())
                .dyeing(reviewParamDto.getDyeing())
                .straightening(reviewParamDto.getStraightening())
                .perm(reviewParamDto.getPerm())
                .lengthStatus(reviewParamDto.getLengthStatus())
                .member(member)
                .build();
    }

    private Member getMember(Long memberId) {
        Member findMember = memberRepository.findByIdAndStatus(memberId, ACTIVE).orElseThrow(
                () -> {
                    throw new ApiException(ApiResponseStatus.INVALID_MEMBER, "유효하지 않은 Member Id로 Member를 조회하려고 했습니다.");
                }
        );

        return findMember;
    }

    private Review getReview(Long reviewId) {
        Review findReview = reviewRepository.findByIdAndStatus(reviewId, ACTIVE).orElseThrow(
                () -> {
                    throw new ApiException(ApiResponseStatus.INVALID_REVIEW, "존재하지 않는 리뷰입니다.");
                }
        );
        return findReview;
    }
}
