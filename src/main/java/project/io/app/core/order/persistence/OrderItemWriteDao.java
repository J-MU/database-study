package project.io.app.core.order.persistence;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import project.io.app.core.order.domain.OrderItem;
import project.io.app.core.order.domain.repository.OrderItemEntityWriteRepository;

public class OrderItemWriteDao implements OrderItemEntityWriteRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderItemWriteDao(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void insertOrderItems(long orderId, List<OrderItem> orderItems) {
        String sql = """
                INSERT INTO ORDER_ITEMS (order_id, product_id, qty) 
                VALUES (:value1, :value2, :value3)
                """;

        MapSqlParameterSource[] batchParams = orderItems.stream()
                .map(item -> {
                    MapSqlParameterSource params = new MapSqlParameterSource();
                    params.addValue("orderId", item.getOrderId());
                    params.addValue("productId", item.getProductId());
                    params.addValue("qty", item.getQty());
                    return params;
                })
                .toArray(MapSqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(sql, batchParams);
    }
}
