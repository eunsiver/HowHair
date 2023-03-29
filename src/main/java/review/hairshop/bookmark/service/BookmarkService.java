package review.hairshop.bookmark.service;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import review.hairshop.bookmark.Bookmark;
import review.hairshop.bookmark.repository.BookmarkRepository;
import review.hairshop.bookmark.responseDto.MyBookMarksResponseDto;
import review.hairshop.common.config.RedisConfig;
import review.hairshop.common.response.ApiException;
import review.hairshop.common.response.ApiResponseStatus;
import review.hairshop.member.Member;
import review.hairshop.member.repository.MemberRepository;
import review.hairshop.reveiwFacade.review.Review;
import review.hairshop.reveiwFacade.review.repository.ReviewRepository;

import java.util.List;
import java.util.stream.Collectors;

import static review.hairshop.common.enums.Status.ACTIVE;
import static review.hairshop.common.enums.Status.INACTIVE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final BookmarkRepository bookmarkRepository;
    private final RedissonClient redissonClient;

    //Redisson을 사용해보고자 했던 고민..
    @Transactional
    public void doBookmarkUseRedisson(Long memberId, Long reviewId) {

        final String lockName="bookmark:lock";
        final RLock lock=redissonClient.getLock(lockName);

        Member member = getMember(memberId);
        Review review = getReview(reviewId);

        //북마크를 한 기록이 있음(북마크 리스트에 존재함)
        if(hasBookmarkRecord(member, review)){

            Bookmark bookmark=bookmarkRepository.findByMemberAndReview(member,review).orElseThrow(
                    ()->{
                        throw new ApiException(ApiResponseStatus.INVALID_BOOKMARK, "존재하지 않는 북마크입니다.");
                    }
            );
            //북마크 O->X
            if(bookmark.getStatus().equals("ACTIVE"))
            {
                bookmark.changeStatus(INACTIVE);
                review.decreaseBookmarkCount();
            }
            //북마크 X->O
            else {
                bookmark.changeStatus(ACTIVE);
                review.increateBookmarkCount();
            }
        }
        //북마크를 한 기록이 없음(북마크 리스트에 존재하지 않음)
        else {
            bookmarkRepository.save(Bookmark.builder().status(ACTIVE).member(member).review(review).build());
            review.increateBookmarkCount();
        }
    }


//    public List<MyBookMarksResponseDto> getMyBookmarkList(Long memberId) {
//        Member member=getMember(memberId);
//        //review review=getReview(reviewId);
//
//        //북마크가 하나도 없을시
//        if(hasBookmarkRecord(member,review)){
//            //어떻게 return 해야 할까요??
//        }
//        //북마크가 존재할 시
//        List<Bookmark> bookmarkList=bookmarkRepository.findByMemberAndActive(member,ACTIVE);
//
//        return createMyBookMarksResponseDto(bookmarkList);
//    }

    private List<MyBookMarksResponseDto> createMyBookMarksResponseDto(List<Bookmark> bookmarkList) {

        return bookmarkList.stream()
                .map(i->MyBookMarksResponseDto.builder()
                        .shopImg("샵 이미지 사진 무엇으로 해야할까요?")
                        .price(i.getReview().getPrice())
                        .shopName(i.getReview().getHairShopName())
                        .straightening(i.getReview().getStraightening())
                        .perm(i.getReview().getPerm())
                        .dyeing(i.getReview().getDyeing())
                        .hairCut(i.getReview().getHairCut())
                        .reviewId(i.getReview().getId())
                        .build())
                .collect(Collectors.toList());
    }

    private boolean hasBookmarkRecord(Member member, Review review){
        
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
