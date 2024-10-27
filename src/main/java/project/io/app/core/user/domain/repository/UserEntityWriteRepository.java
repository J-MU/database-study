package project.io.app.core.user.domain.repository;

import project.io.app.core.user.domain.User;

public interface UserEntityWriteRepository {
    long insertUser(User user);

    void update(Long userId, String nickname);

    void updateWithActiveRecord(Long userId, String nickname);
}
