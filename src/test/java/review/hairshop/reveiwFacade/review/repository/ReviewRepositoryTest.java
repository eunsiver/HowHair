package review.hairshop.reveiwFacade.review.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import review.hairshop.member.Member;
import review.hairshop.member.QMember;
import review.hairshop.reveiwFacade.review.QReview;
import review.hairshop.reveiwFacade.review.Review;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.stream.Collectors;

import static com.querydsl.core.types.ExpressionUtils.count;
import static review.hairshop.bookmark.QBookmark.bookmark;
import static review.hairshop.common.enums.Status.*;
import static review.hairshop.member.QMember.*;
import static review.hairshop.member.QMember.member;
import static review.hairshop.reveiwFacade.review.QReview.*;

@SpringBootTest
@Transactional
class ReviewRepositoryTest {
    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before(){
        queryFactory = new JPAQueryFactory(em);
    }

    @Test
    public void getReivewIdListByBookmarkCount(){
        List<Long> li=  queryFactory
                .select(bookmark.review.id.as("reviewId"))
                .from(bookmark)
                .where(bookmark.status.eq(ACTIVE))
                .groupBy(bookmark.review.id)
                .orderBy(bookmark.review.id.count().desc())
                .limit(10)
                .fetch();

        for(Long i:li){
            System.out.println(i);
        }

        //
        List<Review> reviews= queryFactory
                .selectFrom(review)
                .where(review.id.in(li))
                .fetch();




    }
//    @Test
//    public void review_ReveiwImage(){
//        List<Review> r=queryFactory
//                .selectFrom(review)
//                .distinct()
//                .join(review.reviewImageList,reviewImage).fetchJoin()
//                .fetch();
//
//        for(Review review:r){
//            System.out.print(review.getId());
//            System.out.printf(" %s",review.getReviewImageList().get(0));
//            System.out.println();
//        }
//    }
    @Test
    public void reveiwIdByBookmarkCount(){
        List<Long> reviewIdList = queryFactory
                .select(bookmark.review.id)
                .from(bookmark)
                .where(bookmark.status.eq(ACTIVE))
                .groupBy(bookmark.review.id)
                .orderBy(bookmark.review.id.count().desc())
                .limit(50)
                .fetch();

        List<Review> reviewList= reviewIdList
                .stream()
                .map(reviewId-> queryFactory
                        .selectFrom(review)
                        .where(review.id.eq(reviewId))
                        .fetchOne())
                .collect(Collectors.toList());

        for(Review review:reviewList){
            System.out.print(review.getId());
            System.out.println();
        }


    }
    @Test
    public void reviweByBookmarkCount(){
        List<Tuple>ids=queryFactory
                .select(review.id,count(review.id))
                .from(review)
                .leftJoin(review.bookmarkList, bookmark)
                .where(bookmark.status.eq(ACTIVE),review.status.eq(ACTIVE))
                .on()
                .groupBy(review.id)
                .orderBy(review.id.count().desc())
                .limit(10)
                .fetch();


    }
    @Test
    public void test(Member memberParam){


        List<Long> r=queryFactory
                .select(review.id)
                .from(bookmark)
                .leftJoin(bookmark.review,review)
                .leftJoin(bookmark.member, member)
                .where(review.status.eq(ACTIVE),bookmark.status.eq(ACTIVE), member.status.eq(ACTIVE)
                ,member.gender.eq(memberParam.getGender())
                        ,member.lengthStatus.eq(memberParam.getLengthStatus())
                                .or(member.curlyStatus.eq(memberParam.getCurlyStatus())))
                .groupBy(review.id)
                .orderBy(review.id.count().desc(),review.createdAt.desc())
                .limit(30)
                .fetch();

    }


}