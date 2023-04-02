package review.hairshop.reveiwFacade.review_image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import review.hairshop.common.enums.Status;
import review.hairshop.reveiwFacade.review_image.ReviewImage;

import java.util.Optional;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    Optional<ReviewImage> findFirstByReviewIdAndStatus(Long reviewId, Status status);

    boolean existsByReviewIdAndStatus(Long reviewId, Status status);
}
