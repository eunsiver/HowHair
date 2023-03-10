package review.hairshop.hair_style.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import review.hairshop.hair_style.HairStyle;

@Repository
public interface HairStyleRepository extends JpaRepository<HairStyle, Long> {
}
