package review.hairshop.hair_type_mapping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import review.hairshop.hair_type_mapping.HairTypeMapping;

@Repository
public interface HairTypeMappingRepository extends JpaRepository<HairTypeMapping, Long> {
}
