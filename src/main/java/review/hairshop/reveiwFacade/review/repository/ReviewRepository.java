package review.hairshop.reveiwFacade.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import review.hairshop.common.enums.Status;
import review.hairshop.reveiwFacade.review.Review;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByIdAndStatus(Long reviewId, Status status);

    List<Review> findAllByMemberIdAndStatus(Long memberId, Status active);

    @Query("select distinct r from Review r left join fetch r.member m where r.id = :reviewId")
    Optional<Review> findOneWithMember(@Param("reviewId") Long reviewId);


}
