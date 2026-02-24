package br.com.spring.project_base.entities.order.service;

import br.com.spring.project_base.entities.order.repositorie.OrderRepository;
import br.com.spring.project_base.entities.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class OrderSecurity {

    private final OrderRepository orderRepository;

    public OrderSecurity(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public boolean isOwner(Long orderId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        assert user != null;
        return orderRepository.existsByIdAndUserId(orderId, user.getId());
    }
}
