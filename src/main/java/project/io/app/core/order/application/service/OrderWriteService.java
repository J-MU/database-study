package project.io.app.core.order.application.service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import project.io.app.core.order.application.OrderWriteUseCase;
import project.io.app.core.order.domain.Order;
import project.io.app.core.order.domain.OrderItem;
import project.io.app.core.order.domain.repository.OrderEntityWriteRepository;
import project.io.app.core.order.domain.repository.OrderItemEntityWriteRepository;

public class OrderWriteService implements OrderWriteUseCase {

    private final OrderEntityWriteRepository orderEntityWriteRepository;
    private final OrderItemEntityWriteRepository orderItemEntityWriteRepository;

    public OrderWriteService(
            OrderEntityWriteRepository orderEntityWriteRepository,
            OrderItemEntityWriteRepository orderItemEntityWriteRepository
    ) {
        this.orderEntityWriteRepository = orderEntityWriteRepository;
        this.orderItemEntityWriteRepository = orderItemEntityWriteRepository;
    }

    @Transactional
    @Override
    public long orderItems(Order order, List<OrderItem> items) {
        long orderId = orderEntityWriteRepository.insertOrder(order);
        orderItemEntityWriteRepository.insertOrderItems(orderId, items);

        return orderId;
    }
}
