package project.io.app.core.user.persistence;

import java.util.HashMap;
import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import project.io.app.core.user.domain.User;
import project.io.app.core.user.domain.repository.UserEntityWriteRepository;

@Repository
public class UserWriteDao implements UserEntityWriteRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserWriteDao(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public long insertUser(User user) {
        String sql = """
                INSERT INTO USERS (name, nickname, gender, country_id, created_at)
                VALUES (:name, :nickname, :gender, :countryId, :createdAt)
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", user.getName())
                .addValue("nickname", user.getNickname())
                .addValue("gender", user.getGender().name())
                .addValue("countryId", user.getCountryId())
                .addValue("createdAt", user.getCreatedAt());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});

        return keyHolder.getKey().longValue();
    }

    @Override
    public void update(Long userId, String nickname) {

    }

    @Override
    public void updateWithActiveRecord(Long userId, String nickname) {

    }

    public void deleteAllUsers() {
        String query = "DELETE FROM USERS"; // 테이블 이름에 맞게 수정
        jdbcTemplate.update(query, new HashMap<>());
    }
}
