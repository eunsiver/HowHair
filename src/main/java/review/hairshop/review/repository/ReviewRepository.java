package review.hairshop.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import review.hairshop.review.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
