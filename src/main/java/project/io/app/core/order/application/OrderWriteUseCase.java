package project.io.app.core.order.application;

import java.util.List;
import project.io.app.core.order.domain.Order;
import project.io.app.core.order.domain.OrderItem;

public interface OrderWriteUseCase {

    long orderItems(Order order, List<OrderItem> items);
}
