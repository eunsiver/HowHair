package review.hairshop.reveiwFacade.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import review.hairshop.bookmark.repository.BookmarkRepository;
import review.hairshop.common.enums.Status;
import review.hairshop.common.enums.isReaderSameWriter;
import review.hairshop.common.response.ApiException;
import review.hairshop.common.response.ApiResponseStatus;
import review.hairshop.common.utils.FilesUtil;
import review.hairshop.member.Member;
import review.hairshop.member.repository.MemberRepository;

import review.hairshop.reveiwFacade.dto.HairSearchDto.ReviewSearchCondition;
import review.hairshop.reveiwFacade.dto.responseDto.MainReviewResponseDto;
import review.hairshop.reveiwFacade.dto.responseDto.ReviewListResponseDto;
import review.hairshop.reveiwFacade.dto.responseDto.ReviewDetailResponseDto;
import review.hairshop.reveiwFacade.review.Review;
import review.hairshop.reveiwFacade.review_image.ReviewImage;
import review.hairshop.reveiwFacade.review_image.repository.ReviewImageRepository;
import review.hairshop.reveiwFacade.dto.ReviewParamDto;
import review.hairshop.reveiwFacade.review.repository.ReviewRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static review.hairshop.common.enums.Status.*;
import static review.hairshop.common.enums.isReaderSameWriter.DIFF;
import static review.hairshop.common.enums.isReaderSameWriter.SAME;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;
    private final FilesUtil filesUtil;
    private final ReviewImageRepository reviewImageRepository;


    @Transactional
    public ReviewDetailResponseDto registerReview(Long memberId, ReviewParamDto reviewParamDto) {

        if (!filesUtil.isCorrectFormImageList(reviewParamDto.getImageFiles()))
            throw new ApiException(ApiResponseStatus.WRONG_IMAGE, "이미지 형식이 올바르지 않습니다");

        Member member = getMember(memberId);
        Review review = createReview(reviewParamDto, member);

        reviewRepository.save(review);

        if (reviewParamDto.getImageFiles().isEmpty()) {

            List<String> sampleImagePath = filesUtil.getSampleUrlList();
            return createReveiewResponse(member, review, sampleImagePath);
        }

        /** 2_2.그렇지 않고 함께 등록할 이미지가 하나 이상 존재하면   <항상 DB먼저 수행 후 - File 작업을 수행해야 함>
         * -> 각 이미지들을 저장할 경로를 가진 ReviewImage 엔티티들을 DB에 save한 후 -> 실제 그 경로에 각 사진을 저장한다. */
        List<String> createdImagePath = filesUtil.createImagePath(review.getId(), reviewParamDto.getImageFiles());
        List<ReviewImage> reviewImageList = createdImagePath.stream()
                .map(i -> ReviewImage.builder().review(review).status(ACTIVE).path(i).build())
                .collect(Collectors.toList());

        reviewImageRepository.saveAll(reviewImageList);

        filesUtil.putImageInS3(createdImagePath, reviewParamDto.getImageFiles());
        List<String> awsImagePath = filesUtil.getImageUrlList(createdImagePath);

        return createReveiewResponse(member, review, awsImagePath);
    }

    @Transactional
    public void withdrawReview(Long reviewId, Long memberId) {

        Review review = getReview(reviewId);

        if (hasAuthorityToDelete(memberId, review)) {
            throw new ApiException(ApiResponseStatus.NOT_AUTHORIZED, "작성자가 아니므로 현 게시물을 삭제할 수 없습니다.");
        }
        review.changeStatus(INACTIVE);
    }

    public ReviewDetailResponseDto getReviewDetail(Long loginedMemberId, Long reviewId) {

        Review review = getReview(reviewId);
        Member member = getMember(loginedMemberId);

        List<ReviewImage> reviewImageList = review.getReviewImageList();

        if (reviewImageList.isEmpty()) {
            List<String> reviewSampleImgPaths = filesUtil.getSampleUrlList();
            return createReveiewResponse(member, review, reviewSampleImgPaths);
        }

        List<String> reviewPath = reviewImageList.stream().map(i -> i.getPath()).collect(Collectors.toList());
        List<String> reviewImgPaths = filesUtil.getImageUrlList(reviewPath);

        return createReveiewResponse(member, review, reviewImgPaths);
    }

    public List<ReviewListResponseDto> getMyReviewList(Long memberId) {

        List<Review> reviewList = reviewRepository.findAllByMemberIdAndStatus(memberId, ACTIVE);

        return returnReviewListDto(memberId, reviewList);
    }

    private List<ReviewListResponseDto> returnReviewListDto(Long memberId,List<Review> reviewList){

        Member member= getMember(memberId);

        if (CollectionUtils.isEmpty(reviewList)) {
            return List.of();
        }

        List<String> imageList = getOneImageForReview(reviewList);

        List<ReviewListResponseDto> list =
                IntStream.range(0,reviewList.size())
                        .mapToObj(i-> createReviewListResponse(member,reviewList.get(i),imageList.get(i)))
                        .collect(Collectors.toList());

        return list;
    }

    public List<ReviewListResponseDto> getPopularBookmarkList(Long memberId) {

        int limitNum=30;
        List<Review> reviewList = reviewRepository.findPopularReviewList(limitNum);

        return returnReviewListDto(memberId, reviewList);
    }

    public List<ReviewListResponseDto> getMyType_recommandReviewList(Long memberId) {

        Member member = getMember(memberId);

        int limitNum = 30;

        List<Review> reviewList = reviewRepository.findMyType_RecommandReviewList(member, limitNum);

        return returnReviewListDto(memberId, reviewList);
    }

    public List<ReviewListResponseDto> getSearchReviewList(Long memberId, ReviewSearchCondition condition) {

        List<Review> reviewList = reviewRepository.search(condition);

        return returnReviewListDto(memberId, reviewList);
    }

    public List<ReviewListResponseDto> getHairShopReviewList(Long memberId, String shopName) {

        List<Review> reviewList = reviewRepository.findByHairShopName(shopName);

        return returnReviewListDto(memberId, reviewList);
    }

    public List<MainReviewResponseDto> getMainMyTypeReviewList(Long memberId) {

        Member member = getMember(memberId);

        int limitNum = 30;

        List<Review> reviewList = reviewRepository.findMyType_RecommandReviewList(member, limitNum);

        return null;
    }

    public List<MainReviewResponseDto> getMainBookmarkReviewList() {
        return null;
    }


    private List<String> getOneImageForReview(List<Review> reviewList) {

        return reviewList
                .stream()
                .map(review -> getOneImagePathForReview(review))
                .collect(Collectors.toList());
    }

    private boolean hasAuthorityToDelete(Long memberId, Review review) {

        if (memberId.equals(review.getMember().getId()))
            return true;

        return false;
    }


    private String getOneImagePathForReview(Review review) {

        String imagePath = null;

        if (!reviewImageRepository.existsByReviewIdAndStatus(review.getId(), ACTIVE))
            imagePath = filesUtil.getSampleUrlList().get(0);

        else {
            ReviewImage reviewImage = reviewImageRepository.findFirstByReviewIdAndStatus(review.getId(), ACTIVE).orElseThrow(() ->
                    new ApiException(ApiResponseStatus.NOT_FOUND, "해당 리뷰에 이미지가 없습니다."));

            imagePath = filesUtil.getImageUrlList(List.of(reviewImage.getPath())).get(0);
        }
        return imagePath;
    }


    private Status didIBookmark(Member member, Review review) {

        boolean isBookmarkActive = bookmarkRepository.findByMemberIdAndReviewIdAndStatus(member.getId(), review.getId(), ACTIVE)
                .isPresent();

        if (isBookmarkActive) return ACTIVE;

        return INACTIVE;
    }

    private int getBookmarkCount(Review review) {

        if (CollectionUtils.isEmpty(review.getBookmarkList()))
            return 0;

        return review.getBookmarkList().size();
    }

    private isReaderSameWriter checkReaderEqualWriter(Member member, Review review) {

        if (member.getId().equals(review.getMember().getId()))
            return SAME;

        return DIFF;
    }

    private Member getMember(Long memberId) {

        Member findMember = memberRepository.findByIdAndStatus(memberId, ACTIVE).orElseThrow(() -> {
            throw new ApiException(ApiResponseStatus.INVALID_MEMBER, "유효하지 않은 Member Id로 Member를 조회하려고 했습니다.");
        });

        return findMember;
    }

    private Review getReview(Long reviewId) {

        Review findReview = reviewRepository.findByIdAndStatus(reviewId, ACTIVE).orElseThrow(() -> {
            throw new ApiException(ApiResponseStatus.INVALID_REVIEW, "존재하지 않는 리뷰입니다.");
        });

        return findReview;
    }

    private Review createReview(ReviewParamDto reviewParamDto, Member member) {

        return Review.builder()
                .content(reviewParamDto.getContent())
                .date(reviewParamDto.getDate())
                .status(ACTIVE)
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
                .curlyStatus(reviewParamDto.getCurlyStatus())
                .member(member)
                .build();
    }

    private ReviewListResponseDto createReviewListResponse(Member member, Review review, String imagePath) {

        return ReviewListResponseDto.builder()
                .shopImg(imagePath)
                .shopName(review.getHairShopName())
                .price(review.getPrice())
                .reviewId(review.getId())
                .straightening(review.getStraightening())
                .dyeing(review.getDyeing())
                .hairCut(review.getHairCut())
                .perm(review.getPerm())
                .bookmarkStatus(didIBookmark(member, review))
                .build();
    }

    private ReviewDetailResponseDto createReveiewResponse(Member member, Review review, List<String> imageUrl) {

        return ReviewDetailResponseDto.builder()
                .reviewId(review.getId())
                .isReaderSameWriter(checkReaderEqualWriter(member, review))
                .shopName(review.getHairShopName())
                .imageUrls(imageUrl)
                .satisfaction(review.getSatisfaction())
                .memberName(member.getName())
                .gender(member.getGender())
                .curlyStatus(member.getCurlyStatus())
                .date(review.getDate())
                .designer(review.getDesignerName())
                .createAt(review.getCreatedAt())
                .hairCut(review.getHairCut()).perm(review.getPerm())
                .dyeing(review.getDyeing())
                .lengthStatus(review.getLengthStatus())
                .price(review.getPrice())
                .content(review.getContent())
                .bookmarkCount(getBookmarkCount(review))
                .bookmarkStatus(didIBookmark(member, review))
                .build();
    }
}


//        if (!condition.getHairCutList().isEmpty())
//            reviewList = reviewRepository.findHairCutReviewList();
//
//        else if (hairType.equals(HairType.Perm))
//            reviewList = reviewRepository.findPermReviewList();
//
//        else if (hairType.equals(HairType.Straightening))
//            reviewList = reviewRepository.findStraigtheningReviewList();
//
//        else if (hairType.equals(HairType.Dyeing))
//            reviewList = reviewRepository.findDyeingReviewList();
//
//        else
//            throw new ApiException(ApiResponseStatus.WRONG_HAIR_TYPE, "잘못된 헤어 타입입니다.");
