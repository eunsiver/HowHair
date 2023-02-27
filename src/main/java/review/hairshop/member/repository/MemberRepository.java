package review.hairshop.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import review.hairshop.member.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
}
