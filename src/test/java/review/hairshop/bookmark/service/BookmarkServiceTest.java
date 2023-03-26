package review.hairshop.bookmark.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import review.hairshop.bookmark.Bookmark;
import review.hairshop.common.enums.*;
import review.hairshop.member.Member;
import review.hairshop.member.repository.MemberRepository;
import review.hairshop.reveiwFacade.review.Review;
import review.hairshop.reveiwFacade.review.repository.ReviewRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
public class BookmarkServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired BookmarkService bookmarkService;
    @Autowired ReviewRepository reviewRepository;
    @Autowired
    MemberRepository memberRepository;

//    @BeforeAll
//    static void setup(){
//
//    }

    @Test
    void 북마크() throws Exception {
        //given
        Member member1= createMember("가영");
        Member member2= createMember("나영");
        Member member3= createMember("다영");
        Member member4= createMember("라영");
        Member member5= createMember("마영");

        Review review=createReview(member1);

        //when
        bookmarkService.doBookmark(member2.getId(),review.getId());
        bookmarkService.doBookmark(member3.getId(),review.getId());
        bookmarkService.doBookmark(member1.getId(),review.getId());
        bookmarkService.doBookmark(member4.getId(),review.getId());
        bookmarkService.doBookmark(member5.getId(),review.getId());

        //then
        Review checkReview=reviewRepository.findByIdAndStatus(review.getId(),Status.ACTIVE).orElseThrow();

        assertEquals(checkReview.getBookmarkCount(),5);
    }
    @Test
    void 북마크취소() throws Exception {

        //given
        Member member1= createMember("가영");
        Member member2= createMember("나영");
        Member member3= createMember("다영");
        Member member4= createMember("라영");
        Member member5= createMember("마영");

        Review review=createReview(member1);

        //when
        bookmarkService.doBookmark(member2.getId(),review.getId());
        bookmarkService.doBookmark(member3.getId(),review.getId());
        bookmarkService.doBookmark(member1.getId(),review.getId());
        bookmarkService.doBookmark(member4.getId(),review.getId());
        bookmarkService.doBookmark(member5.getId(),review.getId());

        bookmarkService.cancelBookmark(member2.getId(),review.getId());
        bookmarkService.cancelBookmark(member5.getId(),review.getId());

        //then
        Review checkReview=reviewRepository.findByIdAndStatus(review.getId(),Status.ACTIVE).orElseThrow();

        assertEquals(checkReview.getBookmarkCount(),3);

    }
    private Member createMember(String name) {
         Member member=Member.builder()
                .status(Status.ACTIVE)
                .name(name)
                 .kakaoId(52225l)
                 .curlyStatus(CurlyStatus.CURLY)
                 .lengthStatus(LengthStatus.SHOULDER)
                 .gender(Gender.FEMALE)
                .build();
         memberRepository.save(member);
        em.persist(member);
         return member;
    }
    private Bookmark createBookmark(Member member,Review review) {
       return Bookmark.builder()
               .review(review)
               .member(member)
               .status(Status.ACTIVE)
               .build();
    }
    private Review createReview(Member member) {
        Review review = Review.builder()
                .bookmarkCout(0)
                .member(member)
                .status(Status.ACTIVE)
                .content("좋아요")
                .date(LocalDate.now())
                .designerName("홍홍")
                .dyeing(Dyeing.NONE)
                .hairCut(Hair_Cut.NONE)
                .hairShopName("헤어샵")
                .price(553331L)
                .lengthStatus(LengthStatus.SHOULDER)
                .perm(Perm.글램펌)
                .satisfaction(Satisfaction.FIVE)
                .straightening(Straightening.NONE)
                .build();
        reviewRepository.save(review);
        em.persist(review);
        return review;
    }
}