package project.io.app.core.user.persistence;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import project.io.app.core.user.domain.Gender;
import project.io.app.core.user.domain.MonthlyNewUserStatistics;
import project.io.app.core.user.domain.MonthlyNewUserStatisticsByGender;
import project.io.app.core.user.domain.User;
import project.io.app.core.user.domain.repository.UserEntityReadRepository;

import java.util.Optional;

@Repository
public class UserReadDao implements UserEntityReadRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserReadDao(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public Optional<User> findById(final Long userId) {
        String query = """
                SELECT *
                FROM USERS
                WHERE id = :userId;
                """;

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId);

        User user = jdbcTemplate.queryForObject(query, params, userRowMapper());

        return Optional.ofNullable(user);
    }

    public List<User> findActiveUsers() {
        String query = """
                SELECT users.*
                FROM users
                JOIN orders o on users.id = o.user_id AND o.created_at >=DATE_SUB(NOW(), INTERVAL 1 YEAR)
                GROUP BY users.id;
                """;

        return jdbcTemplate.query(query, userRowMapper());
    }

    public List<User> getAnnualActiveUserCountInKorean() {
        String query = """
                SELECT users.*
                FROM users
                JOIN orders o on users.id = o.user_id AND o.created_at >=DATE_SUB(NOW(), INTERVAL 1 YEAR)
                GROUP BY users.id;
                """;

        return jdbcTemplate.query(query, userRowMapper());
    }

    public List<User> findUsersByPurchaseAmountInRecentOneYear(Long threshold) {
        String query = """
                SELECT
                    users.*
                FROM users
                JOIN orders o on users.id = o.user_id AND o.created_at >=DATE_SUB(NOW(), INTERVAL 1 YEAR)
                GROUP BY users.id
                HAVING SUM(o.total_amount)>=:threshold;
                """;

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("threshold", threshold);

        return jdbcTemplate.query(query, userRowMapper());
    }

    public List<User> findUsersWithRecentPurchasesOrderedBySignupDate() {
        String query = """
                SELECT
                    DISTINCT users.id,
                    users.country_id,
                    users.name,
                    users.nickname,
                    users.gender,
                    users.created_at,
                    users.last_modified_at,
                    users.deleted
                FROM users
                JOIN orders o on users.id = o.user_id AND o.created_at >=DATE_SUB(NOW(), INTERVAL 3 MONTH)
                ORDER BY users.id
                LIMIT 10;
                """;

        return jdbcTemplate.query(query, userRowMapper());
    }

    public List<User> findUsersWithMultiplePurchasesOfSameProductOnDifferentDates() {
        String query = """
                SELECT
                    o.user_id,
                    COUNT(*) AS order_count,
                    DATE(o.created_at)
                FROM USERS u
                JOIN ORDERS o ON o.user_id = u.id
                JOIN ORDER_ITEMS oi ON o.id = oi.order_id
                GROUP BY o.user_id, oi.product_id, DATE(o.created_at)
                HAVING order_count >= 2;
                """;

        return jdbcTemplate.query(query, userRowMapper());
    }

    public List<User> findAnnualVipUserCounts() {
        String query = """
                SELECT
                    COUNT(user_id) as vip_count,
                    purchase_statistics.purchase_year-1 as year
                FROM (
                    SELECT
                        user_id,
                        SUM(total_amount)  as total_amount_in_year,
                        YEAR(o.created_at) as purchase_year
                    FROM orders o
                    GROUP BY purchase_year, o.user_id
                    HAVING total_amount_in_year >= 10000000
                      ) purchase_statistics
                GROUP BY purchase_statistics.purchase_year;
                """;

        return jdbcTemplate.query(query, userRowMapper());
    }


    private RowMapper<User> userRowMapper() {
        return ((result, rowNum) ->
                User.builder()
                        .id(result.getLong("id"))
                        .name(result.getString("name"))
                        .nickname(result.getString("nickname"))
                        .gender(Gender.valueOf(result.getString("gender")))
                        .countryId(result.getLong("country_id"))
                        .build()
        );
    }

    public List<MonthlyNewUserStatistics> getMonthlyNewUserCountsDescending() {
        String query = """
                SELECT
                    DATE_FORMAT(created_at,"%Y-%m") as incoming_date,
                    COUNT(*) as incoming_user_count
                FROM users
                WHERE created_at>=DATE_SUB(NOW(), INTERVAL 1 YEAR)
                GROUP BY YEAR(created_at),MONTH(created_at)
                ORDER BY incoming_user_count DESC;
                """;

        List<MonthlyNewUserStatistics> statistics = jdbcTemplate.query(query, monthlyNewUserStatisticsMapper());

        return statistics;
    }

    private RowMapper<MonthlyNewUserStatistics> monthlyNewUserStatisticsMapper() {
        return ((result, rowNum) ->
                new MonthlyNewUserStatistics(
                        result.getString("incoming_date"),
                        result.getLong("incoming_user_count")
                )
        );
    }

    public List<MonthlyNewUserStatisticsByGender> getMonthlyNewUserCountsDescendingByGender() {
        String query = """
                SELECT
                	DATE_FORMAT(created_at,"%Y-%m") as incoming_date,
                	gender,
                	COUNT(*) as incoming_user_count
                FROM users
                WHERE created_at>=DATE_SUB(NOW(), INTERVAL 1 YEAR)
                GROUP BY YEAR(created_at),MONTH(created_at), gender
                ORDER BY incoming_user_count DESC;
                """;

        List<MonthlyNewUserStatisticsByGender> statistics = jdbcTemplate.query(query,
                monthlyNewUserStatisticsByGenderMapper());

        return statistics;
    }

    private RowMapper<MonthlyNewUserStatisticsByGender> monthlyNewUserStatisticsByGenderMapper() {
        return ((result, rowNum) ->
                new MonthlyNewUserStatisticsByGender(
                        result.getString("incoming_date"),
                        result.getLong("incoming_user_count"),
                        Gender.valueOf(result.getString("gender"))
                )
        );
    }
}
