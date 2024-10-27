package project.io.app.core.order.persistence;

import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import project.io.app.core.order.domain.Order;
import project.io.app.core.order.domain.repository.OrderEntityWriteRepository;

@Repository
public class OrderWriteDao implements OrderEntityWriteRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderWriteDao(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public long insertOrder(Order order) {
        String sql = """
                INSERT INTO ORDERS(user_id, total_amount, created_at)
                VALUES (:userId, :totalAmount, :createdAt)
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", order.getUserId())
                .addValue("totalAmount", order.getTotalAmount())
                .addValue("createdAt", order.getCreatedAt());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});

        return keyHolder.getKey().longValue();
    }
}
