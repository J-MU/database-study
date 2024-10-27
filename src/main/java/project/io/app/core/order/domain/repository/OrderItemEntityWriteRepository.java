package project.io.app.core.order.domain.repository;

import java.util.List;
import project.io.app.core.order.domain.OrderItem;

public interface OrderItemEntityWriteRepository {

    void insertOrderItems(long orderId, List<OrderItem> orderItems);
}
