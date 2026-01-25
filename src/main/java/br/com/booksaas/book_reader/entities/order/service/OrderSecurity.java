package br.com.booksaas.book_reader.entities.order.service;

import br.com.booksaas.book_reader.entities.order.repositorie.OrderRepository;
import br.com.booksaas.book_reader.entities.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class OrderSecurity {

    private final OrderRepository orderRepository;

    public OrderSecurity(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public boolean isOwner(Long bookId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        assert user != null;
        return orderRepository.existsByIdAndUserId(bookId, user.getId());
    }
}
