package br.com.booksaas.book_reader.order.service;

import br.com.booksaas.book_reader.order.entity.Order;
import br.com.booksaas.book_reader.user.entity.User;
import br.com.booksaas.book_reader.events.BeforeDeleteUser;
import br.com.booksaas.book_reader.order.dto.OrderDTO;
import br.com.booksaas.book_reader.order.repositorie.OrderRepository;
import br.com.booksaas.book_reader.user.repositorie.UserRepository;
import br.com.booksaas.book_reader.util.NotFoundException;
import br.com.booksaas.book_reader.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderService(final OrderRepository orderRepository,
            final UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public List<OrderDTO> findAll() {
        final List<Order> orders = orderRepository.findAll(Sort.by("id"));
        return orders.stream()
                .map(order -> mapToDTO(order, new OrderDTO()))
                .toList();
    }

    public OrderDTO get(final Long id) {
        return orderRepository.findById(id)
                .map(order -> mapToDTO(order, new OrderDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final OrderDTO orderDTO) {
        final Order order = new Order();
        mapToEntity(orderDTO, order);
        return orderRepository.save(order).getId();
    }

    public void update(final Long id, final OrderDTO orderDTO) {
        final Order order = orderRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(orderDTO, order);
        orderRepository.save(order);
    }

    public void delete(final Long id) {
        final Order order = orderRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        orderRepository.delete(order);
    }

    private OrderDTO mapToDTO(final Order order, final OrderDTO orderDTO) {
        orderDTO.setId(order.getId());
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setPaymentMethod(order.getPaymentMethod());
        orderDTO.setTransactionId(order.getTransactionId());
        orderDTO.setCreatedAt(order.getCreatedAt());
        orderDTO.setUser(order.getUser() == null ? null : order.getUser().getId());
        return orderDTO;
    }

    private Order mapToEntity(final OrderDTO orderDTO, final Order order) {
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setStatus(orderDTO.getStatus());
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setTransactionId(orderDTO.getTransactionId());
        order.setCreatedAt(orderDTO.getCreatedAt());
        final User user = orderDTO.getUser() == null ? null : userRepository.findById(orderDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        order.setUser(user);
        return order;
    }

    @EventListener(BeforeDeleteUser.class)
    public void on(final BeforeDeleteUser event) {
        final ReferencedException referencedException = new ReferencedException();
        final Order userOrder = orderRepository.findFirstByUserId(event.getId());
        if (userOrder != null) {
            referencedException.setKey("user.order.user.referenced");
            referencedException.addParam(userOrder.getId());
            throw referencedException;
        }
    }

}
