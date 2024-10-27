package project.io.app.core.user.domain.repository;

import java.util.Optional;
import project.io.app.core.user.domain.User;

public interface UserEntityReadRepository {
    Optional<User> findById(Long userId);
}
