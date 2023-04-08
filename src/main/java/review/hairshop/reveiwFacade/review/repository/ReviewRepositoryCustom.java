package review.hairshop.reveiwFacade.review.repository;

import review.hairshop.member.Member;
import review.hairshop.reveiwFacade.dto.HairSearchDto.*;
import review.hairshop.reveiwFacade.review.Review;


import java.util.List;

public interface ReviewRepositoryCustom {
    List<Review> findPopularReviewList(int limitNum);

    List<Review> findMyType_RecommandReviewList(Member memberParam,int limitNum);

    List<Review> findByHairShopName(String hairShopName);

    List<Review> search(ReviewSearchCondition condition);


//    List<Review> cutSearch(CutReviewCondition dto);
//
//    List<Review> permSearch(PermReviewCondition dto);
//
//    List<Review> dyeingSearch(DyeingReviewCondition dto);
//
//    List<Review> straighteningSearch(StraighteningReviewCondition dto);

    //    List<Review> findHairCutReviewList();
//
//    List<Review> findPermReviewList();
//
//    List<Review> findStraigtheningReviewList();
//
//    List<Review> findDyeingReviewList();
//

}
