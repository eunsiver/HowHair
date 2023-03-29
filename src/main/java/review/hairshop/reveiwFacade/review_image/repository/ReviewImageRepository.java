package review.hairshop.reveiwFacade.review_image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import review.hairshop.common.enums.Status;
import review.hairshop.reveiwFacade.review_image.ReviewImage;

import java.util.List;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    List<ReviewImage> findAllByReviewIdAndStatus(Long reviewId, Status status);
}
