package review.hairshop.reveiwFacade.review.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import review.hairshop.common.enums.SearchFilter;
import review.hairshop.common.enums.hairStyle.Dyeing;
import review.hairshop.common.enums.hairStyle.Hair_Cut;
import review.hairshop.common.enums.hairStyle.Perm;
import review.hairshop.common.enums.hairStyle.Straightening;
import review.hairshop.common.enums.memberDefaultInfo.CurlyStatus;
import review.hairshop.common.enums.memberDefaultInfo.Gender;
import review.hairshop.common.enums.memberDefaultInfo.LengthStatus;
import review.hairshop.member.Member;
import review.hairshop.reveiwFacade.dto.HairSearchDto.*;
import review.hairshop.reveiwFacade.review.Review;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;
import static review.hairshop.bookmark.QBookmark.bookmark;
import static review.hairshop.common.enums.Status.ACTIVE;
import static review.hairshop.member.QMember.*;
import static review.hairshop.reveiwFacade.review.QReview.review;

public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Review> findPopularReviewList(int limitNum) {

        List<Long> reviewIdList = getPopularReviewIdList_ByBookmark(limitNum);

        return findReviewList_byReviewIdList(reviewIdList);
    }
    private List<Long> getPopularReviewIdList_ByBookmark(int limitNum){
        return queryFactory
                .select(review.id)
                .from(bookmark)
                .leftJoin(bookmark.review, review)
                .where(bookmark.status.eq(ACTIVE), review.status.eq(ACTIVE))
                .groupBy(review.id)
                .orderBy(review.id.count().desc(), review.createdAt.desc())
                .limit(limitNum)
                .fetch();
    }

    @Override
    public List<Review> findMyType_RecommandReviewList(Member memberParam,int limitNum) {

        List<Long> reviewIdList = getMyTypeReviewIdList_ByBookmark(memberParam,limitNum);

        return findReviewList_byReviewIdList(reviewIdList);
    }

    private List<Long> getMyTypeReviewIdList_ByBookmark(Member memberParam, int limitNum){

        return queryFactory
                .select(bookmark.review.id)
                .from(bookmark)
                .leftJoin(bookmark.member, member)
                .where(bookmark.status.eq(ACTIVE), member.status.eq(ACTIVE)
                        , member.curlyStatus.eq(memberParam.getCurlyStatus())
                        , member.lengthStatus.eq(memberParam.getLengthStatus())
                        , member.curlyStatus.eq(memberParam.getCurlyStatus()))
                .groupBy(bookmark.review.id)
                .orderBy(bookmark.review.id.count().desc())
                .limit(limitNum)
                .fetch();
    }


    public List<Review> findReviewList_byReviewIdList(List<Long> reviewIdList) {
        List<Review> reviewList = reviewIdList
                .stream()
                .map(reviewId -> queryFactory
                        .selectFrom(review)
                        .where(review.id.eq(reviewId))
                        .fetchOne())
                .collect(Collectors.toList());

        return reviewList;
    }

    @Override
    public List<Review> findByHairShopName(String hairShopName) {
        return queryFactory
                .selectFrom(review)
                .where(review.status.eq(ACTIVE), review.hairShopName.like(hairShopName))
                .fetch();
    }

    @Override
    public List<Review> search(ReviewSearchCondition condition) {

        return queryFactory
                .selectFrom(review)
                .leftJoin(review.member, member)
                .where(genderEq(condition.getGender()), lengthStatusEq(condition.getLengthStatus())
                        , curlyStatusEq(condition.getCurlyStatus()), hairCutIn(condition.getHairCutList())
                        , permIn(condition.getPermList()), dyeingIn(condition.getDyeingList())
                        , straighteningIn(condition.getStraighteningList())
                        , startPriceGoe(condition.getStartPrice()), endPriceLoe(condition.getEndPrice())
                        , member.status.eq(ACTIVE), review.status.eq(ACTIVE))
                .orderBy(getOrder(condition.getSearchFilter()))
                .fetch();
    }


    private BooleanExpression genderEq(Gender gender) {
        return isEmpty(gender) ? null : member.gender.eq(gender);
    }

    private BooleanExpression lengthStatusEq(LengthStatus lengthStatus) {
        return isEmpty(lengthStatus) ? null : review.lengthStatus.eq(lengthStatus);
    }

    private BooleanExpression curlyStatusEq(CurlyStatus curlyStatus) {
        return isEmpty(curlyStatus) ? null : review.curlyStatus.eq(curlyStatus);
    }

    private BooleanExpression hairCutIn(List<Hair_Cut> haircut) {

        if (isEmpty(haircut)) return null;

        else if (haircut.contains(Hair_Cut.ALL))
            return review.hairCut.ne(Hair_Cut.NONE);

        else return review.hairCut.in(haircut);
    }

    private BooleanExpression permIn(List<Perm> perm) {

        if (isEmpty(perm)) return null;

        else if (perm.contains(Perm.ALL))
            return review.perm.ne(Perm.NONE);

        else return review.perm.in(perm);
    }

    private BooleanExpression dyeingIn(List<Dyeing> dyeing) {

        if (isEmpty(dyeing)) return null;

        else if (dyeing.contains(Dyeing.ALL))
            return review.dyeing.ne(Dyeing.NONE);

        else return review.dyeing.in(dyeing);
    }

    private BooleanExpression straighteningIn(List<Straightening> straightening) {

        if (isEmpty(straightening))
            return null;

        else if(straightening.contains(Straightening.ALL))
            return review.straightening.ne(Straightening.NONE);

        return review.straightening.in(straightening);
    }

    private BooleanExpression startPriceGoe(Integer startPrice) {
        return isEmpty(startPrice) ? null : review.price.goe(startPrice);
    }

    private BooleanExpression endPriceLoe(Integer endPrice) {
        return isEmpty(endPrice) ? null : review.price.loe(endPrice);
    }

    private OrderSpecifier<?> getOrder(SearchFilter searchFilter) {

        if (isEmpty(searchFilter)) {
            return review.createdAt.desc();
        } else if (searchFilter.equals(SearchFilter.high_Satisfaction)) {
            return review.satisfaction.desc();
        } else if (searchFilter.equals(SearchFilter.low_Satisfaction)) {
            return review.satisfaction.asc();
        } else if (searchFilter.equals(SearchFilter.latest)) {
            return review.createdAt.desc();
        }
        return review.createdAt.desc();
    }

    //
//    @Override
//    public List<Review> findHairCutReviewList() {
//        return queryFactory
//                .selectFrom(review)
//                .where(review.status.eq(ACTIVE), review.hairCut.ne(Hair_Cut.NONE))
//                .fetch();
//    }
//
//    @Override
//    public List<Review> findPermReviewList() {
//        return queryFactory
//                .selectFrom(review)
//                .where(review.status.eq(ACTIVE), review.perm.ne(Perm.NONE))
//                .fetch();
//    }
//
//    @Override
//    public List<Review> findStraigtheningReviewList() {
//        return queryFactory
//                .selectFrom(review)
//                .where(review.status.eq(ACTIVE), review.straightening.ne(Straightening.NONE))
//                .fetch();
//    }
//
//    @Override
//    public List<Review> findDyeingReviewList() {
//        return queryFactory
//                .selectFrom(review)
//                .where(review.status.eq(ACTIVE), review.dyeing.ne(Dyeing.NONE))
//                .fetch();
//    }
//


//    @Override
//    public List<Review> permSearch(PermReviewCondition dto) {
//        return queryFactory
//                .selectFrom(review)
//                .leftJoin(review.member, member)
//                .where(genderEq(dto.getGender()), lengthStatusEq(dto.getLengthStatus())
//                        , curlyStatusEq(dto.getCurlyStatus()), permIn(dto.getPermList())
//                        , startPriceGoe(dto.getStartPrice()), endPriceLoe(dto.getEndPrice())
//                        , member.status.eq(ACTIVE), review.status.eq(ACTIVE))
//                .orderBy(getOrder(dto.getSearchFilter()))
//                .fetch();
//    }
//
//    @Override
//    public List<Review> dyeingSearch(DyeingReviewCondition dto) {
//        return queryFactory
//                .selectFrom(review)
//                .leftJoin(review.member, member)
//                .where(genderEq(dto.getGender()), lengthStatusEq(dto.getLengthStatus())
//                        , curlyStatusEq(dto.getCurlyStatus()), dyeingIn(dto.getDyeingList())
//                        , startPriceGoe(dto.getStartPrice()), endPriceLoe(dto.getEndPrice())
//                        , member.status.eq(ACTIVE), review.status.eq(ACTIVE))
//                .orderBy(getOrder(dto.getSearchFilter()))
//                .fetch();
//    }
//
//    @Override
//    public List<Review> straighteningSearch(StraighteningReviewCondition dto) {
//        return queryFactory
//                .selectFrom(review)
//                .leftJoin(review.member, member)
//                .where(genderEq(dto.getGender()), lengthStatusEq(dto.getLengthStatus())
//                        , curlyStatusEq(dto.getCurlyStatus()), straighteningIn(dto.getStraighteningList())
//                        , startPriceGoe(dto.getStartPrice()), endPriceLoe(dto.getEndPrice())
//                        , member.status.eq(ACTIVE), review.status.eq(ACTIVE))
//                .orderBy(getOrder(dto.getSearchFilter()))
//                .fetch();
//    }

    //    @Override
//    public List<Review> findPopularReviewList() {
//        List<Long> reviewIdList_byBookmark = queryFactory
//                .select(bookmark.review.id)
//                .from(bookmark)
//                .where(bookmark.status.eq(ACTIVE))
//                .groupBy(bookmark.review.id)
//                .orderBy(bookmark.review.id.count().desc())
//                .limit(30)
//                .fetch();
//
//        List<Review> reviewList= reviewIdList_byBookmark
//                .stream()
//                .map(reviewId-> queryFactory
//                        .selectFrom(review)
//                        .where(review.id.eq(reviewId),review.status.eq(ACTIVE))
//                        .fetchOne())
//                .collect(Collectors.toList());
//
//        return reviewList;
//    }

}
