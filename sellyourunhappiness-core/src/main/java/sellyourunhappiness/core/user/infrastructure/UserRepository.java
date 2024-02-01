package sellyourunhappiness.core.user.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sellyourunhappiness.core.user.domain.User;
import sellyourunhappiness.core.user.domain.enums.SocialType;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
	Optional<User> findBySocialTypeAndEmail(SocialType socialType, String email);
}