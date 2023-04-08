package review.hairshop.reveiwFacade.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import review.hairshop.common.enums.Status;
import review.hairshop.reveiwFacade.review.Review;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom{

    Optional<Review> findByIdAndStatus(Long reviewId, Status status);
    List<Review> findAllByMemberIdAndStatus(Long memberId, Status active);
}
