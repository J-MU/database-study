package project.io.app.core.order.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.io.app.core.common.BaseEntity;

import java.util.Objects;

@NoArgsConstructor
@Getter
public class OrderItem extends BaseEntity {
    private Long id;
    private Long orderId;
    private Long productId;
    private int qty;

    private OrderItem() {
    }

    @Builder
    private OrderItem(
        final Long id,
        final Long orderId,
        final Long productId,
        final int qty
    ) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.qty = qty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
