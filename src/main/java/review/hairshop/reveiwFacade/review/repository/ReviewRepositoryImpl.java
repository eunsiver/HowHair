package review.hairshop.reveiwFacade.review.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import review.hairshop.reveiwFacade.dto.HairShopDto;
import review.hairshop.reveiwFacade.dto.responseDto.ReviewListResponseDto;
import review.hairshop.reveiwFacade.review.QReview;

import javax.persistence.EntityManager;
import java.util.List;

import static review.hairshop.reveiwFacade.review.QReview.review;


public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<HairShopDto> OrderShopByReviewCount() {

        QReview r = new QReview(review);

        return queryFactory
                .select(Projections.fields(HairShopDto.class,r.hairShopName))
                .distinct()
                .from(r)
                .groupBy(r.hairShopName)
                .orderBy(r.hairShopName.count().desc())
                .fetch();
    }

    @Override
    public List<ReviewListResponseDto> OrderReviewByBookmarkCount() {
        return null;
    }

}
