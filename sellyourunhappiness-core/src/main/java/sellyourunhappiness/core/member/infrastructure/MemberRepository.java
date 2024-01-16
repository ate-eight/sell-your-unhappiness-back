package sellyourunhappiness.core.member.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import sellyourunhappiness.core.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}