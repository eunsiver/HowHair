package review.hairshop.bookmark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import review.hairshop.bookmark.Bookmark;
import review.hairshop.common.enums.Status;
import review.hairshop.member.Member;
import review.hairshop.reveiwFacade.review.Review;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    boolean existsByMemberAndReview(Member member, Review review);
    Optional<Bookmark> findByMemberAndReview(Member member, Review review);

    List<Bookmark> findByMemberAndStatus(Member member, Status status);

    int countByReviewIdAndStatus(Long ReviewId, Status active);
}
