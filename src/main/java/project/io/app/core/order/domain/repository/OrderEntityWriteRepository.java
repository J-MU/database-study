package project.io.app.core.order.domain.repository;

import project.io.app.core.order.domain.Order;
import project.io.app.core.user.domain.User;

public interface OrderEntityWriteRepository {
    long insertOrder(Order user);
}
