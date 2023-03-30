package review.hairshop.bookmark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import review.hairshop.bookmark.Bookmark;
import review.hairshop.bookmark.repository.BookmarkRepository;
import review.hairshop.bookmark.responseDto.MyBookMarksResponseDto;
import review.hairshop.common.response.ApiException;
import review.hairshop.common.response.ApiResponseStatus;
import review.hairshop.common.utils.FilesUtil;
import review.hairshop.member.Member;
import review.hairshop.member.repository.MemberRepository;
import review.hairshop.reveiwFacade.review.Review;
import review.hairshop.reveiwFacade.review.repository.ReviewRepository;
import review.hairshop.reveiwFacade.review_image.ReviewImage;
import review.hairshop.reveiwFacade.review_image.repository.ReviewImageRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static review.hairshop.common.enums.Status.ACTIVE;
import static review.hairshop.common.enums.Status.INACTIVE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final BookmarkRepository bookmarkRepository;

    private final FilesUtil filesUtil;

    @Transactional
    public void doOnOffBookmark(Long memberId, Long reviewId) {


        Optional<Bookmark> byMemberIdAndReviewId = bookmarkRepository.findByMemberIdAndReviewId(memberId, reviewId);

        byMemberIdAndReviewId.ifPresentOrElse(

                Bookmark -> {
                    if (byMemberIdAndReviewId.get().getStatus().equals(ACTIVE)) byMemberIdAndReviewId.get().changeStatus(INACTIVE);

                    else byMemberIdAndReviewId.get().changeStatus(ACTIVE);
                },
                    () -> {

                    Member member = getMember(memberId);
                    Review review = getReview(reviewId);
                    bookmarkRepository.save(Bookmark.builder().status(ACTIVE).member(member).review(review).build());

                });
    }


    public List<MyBookMarksResponseDto> getMyBookmarkList(Long memberId) {

        Member member = getMember(memberId);

        //member와 Active 상태인 북마크가 없으면 List<MyBookMarksResponseDto>에 빈 리스트를 리턴해준다.
        if (!bookmarkRepository.existsByMemberAndStatus(member, ACTIVE)) {
            return List.of();
        }
        //member와 Active 상태인 북마크가 있으면 List<MyBookMarksResponseDto>에 북마크 리스트를 리턴해준다.
        List<Bookmark> bookmarkList = bookmarkRepository.findByMemberAndStatus(member, ACTIVE);

        return bookmarkList.stream()
                .map(this::createMyBookMarksResponseDto)
                .collect(Collectors.toList());
    }

    private MyBookMarksResponseDto createMyBookMarksResponseDto(Bookmark bookmark) {

        String imagePath;
        Review review = bookmark.getReview();

        if (!reviewImageRepository.existsByReviewIdAndStatus(review.getId(), ACTIVE))
            imagePath = filesUtil.getSampleUrlList().get(0);

        else {
            ReviewImage reviewImage = reviewImageRepository.findFirstByReviewIdAndStatus(review.getId(), ACTIVE)
                    .orElseThrow(() -> new ApiException(ApiResponseStatus.NOT_FOUND, "해당 리뷰에 이미지가 없습니다."));
            imagePath = filesUtil.getImageUrlList(List.of(reviewImage.getUrl())).get(0);
        }

        return MyBookMarksResponseDto.builder()
                .shopImg(imagePath)
                .price(bookmark.getReview().getPrice())
                .shopName(bookmark.getReview().getHairShopName())
                .straightening(bookmark.getReview().getStraightening())
                .perm(bookmark.getReview().getPerm())
                .dyeing(bookmark.getReview().getDyeing())
                .hairCut(bookmark.getReview().getHairCut())
                .reviewId(bookmark.getReview().getId())
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
