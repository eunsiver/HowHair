package review.hairshop.hair_style_mapping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import review.hairshop.hair_style_mapping.HairStyleMapping;

@Repository
public interface HairStyleMappingRepository extends JpaRepository<HairStyleMapping, Long> {
}
