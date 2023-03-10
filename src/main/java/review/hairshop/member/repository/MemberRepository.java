package review.hairshop.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import review.hairshop.common.enums.Status;
import review.hairshop.member.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByKakaoId(Long kakaoId);

    boolean existsByIdAndStatus(long parseLong, Status status);

    boolean existsByKakaoId(Long kakaoId);
//    boolean existsByIdxAndStatus(Long memberIdx, Status status);
//

}
