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

        List<Long> reviewIdList = getReviewIdList_OrderByBookmark(limitNum);

        List<Review> reviewList = findReviewList_byReviewIdList(reviewIdList);

        return reviewList;
    }

    @Override
    public List<Review> findMyType_RecommandReviewList(Member memberParam, int limitNum) {

        // 북마크, 리뷰, 멤버 조인
        //다 active하고, member와 gender 같고, review의 머리길이가 같거나, 곱슬정도가 같거나 한 것들을
        //group by order by 해서 reviewId만 뽑아냄
        //그 후 reviewList 추출

        List<Long> reviewIdList = queryFactory
                .select(review.id)
                .from(bookmark)
                .leftJoin(bookmark.review, review)
                .leftJoin(bookmark.member, member)
                .where(review.status.eq(ACTIVE), bookmark.status.eq(ACTIVE), member.status.eq(ACTIVE)
                        , member.gender.eq(memberParam.getGender())
                        , review.lengthStatus.eq(memberParam.getLengthStatus())
                                .or(review.curlyStatus.eq(memberParam.getCurlyStatus())))
                .groupBy(review.id)
                .orderBy(review.id.count().desc(), review.createdAt.desc())
                .limit(limitNum)
                .fetch();

        List<Review> reviewList = findReviewList_byReviewIdList(reviewIdList);

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
                        , curlyStatusEq(condition.getCurlyStatus()), hairCutIn(condition.getHairCut())
                        , permIn(condition.getPerm()), dyeingIn(condition.getDyeing())
                        , straighteningIn(condition.getStraightening())
                        , startPriceGoe(condition.getStartPrice()), endPriceLoe(condition.getEndPrice())
                        , member.status.eq(ACTIVE), review.status.eq(ACTIVE))
                .orderBy(getOrder(condition.getSearchFilter()))
                .fetch();
    }

    /***********************************************************************************************/

    private List<Long> getReviewIdList_OrderByBookmark(int limitNum) {

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

    /********************************************************************************/

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

        else if (straightening.contains(Straightening.ALL))
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
}

//        List<Review> MyTypeReviewList = reviewIdList
//                .stream()
//                .map(reviewId -> queryFactory
//                        .selectFrom(review)
//                        .where(review.member.gender.eq(memberParam.getGender())
//                        ,review.lengthStatus.eq(memberParam.getLengthStatus())
//                                        .or(review.curlyStatus.eq(memberParam.getCurlyStatus())))
//                        .fetchOne())
//                .collect(Collectors.toList());