package review.hairshop.bookmark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import review.hairshop.bookmark.Bookmark;
import review.hairshop.bookmark.repository.BookmarkRepository;
import review.hairshop.common.response.ApiException;
import review.hairshop.common.response.ApiResponseStatus;
import review.hairshop.member.Member;
import review.hairshop.member.repository.MemberRepository;
import review.hairshop.reveiwFacade.review.Review;
import review.hairshop.reveiwFacade.review.repository.ReviewRepository;

import static review.hairshop.common.enums.Status.ACTIVE;
import static review.hairshop.common.enums.Status.INACTIVE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public void doBookmark(Long memberId, Long reviewId) {

        Member member = getMember(memberId);
        Review review = getReview(reviewId);

        if(beforeBookmarkRecord(member, review)){
            //이전에 북마크했던 경험이 있다면
            Bookmark bookmark=bookmarkRepository.findByMemberAndReview(member,review).orElseThrow(
                    ()->{
                        throw new ApiException(ApiResponseStatus.INVALID_BOOKMARK, "존재하지 않는 북마크입니다.");
                    }
            );
            if(bookmark.getStatus().equals("ACTIVE")){
                throw new ApiException(ApiResponseStatus.INVALID_BOOKMARK,"이미 좋아요를 누른 사람입니다.");
            }

            bookmark.changeStatus(ACTIVE);

        } else {
            //이전에 북마크했던 경험이 없다면
            bookmarkRepository.save(Bookmark.builder().status(ACTIVE).member(member).review(review).build());
        }
        review.increateBookmarkCount();
    }

    @Transactional
    public void cancelBookmark(Long memberId, Long reviewId) {

        Member member = getMember(memberId);
        Review review = getReview(reviewId);

        Bookmark bookmark=bookmarkRepository.findByMemberAndReview(member,review).orElseThrow(
                ()->{
                    throw new ApiException(ApiResponseStatus.INVALID_BOOKMARK, "존재하지 않는 북마크입니다.");
                }
        );
        bookmark.changeStatus(INACTIVE);
        review.decreaseBookmarkCount();
    }

    private boolean beforeBookmarkRecord(Member member, Review review){
        
        return bookmarkRepository.existsByMemberAndReview(member,review);
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
