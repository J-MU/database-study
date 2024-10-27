package project.io.app.core.order.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.io.app.core.common.BaseEntity;

import java.math.BigDecimal;
import java.util.Objects;

@NoArgsConstructor
@Getter
public class Order extends BaseEntity {
    private Long id;
    private Long userId;
    private BigDecimal totalAmount;

    @Builder
    private Order(Long id, Long userId, BigDecimal totalAmount) {
        this.id = id;
        this.userId = userId;
        this.totalAmount = totalAmount;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
