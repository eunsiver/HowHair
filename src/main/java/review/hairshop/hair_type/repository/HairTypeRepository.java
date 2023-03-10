package review.hairshop.hair_type.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import review.hairshop.common.enums.Hair_Type;
import review.hairshop.hair_type.HairType;

@Repository
public interface HairTypeRepository extends JpaRepository<HairType, Long> {
}
