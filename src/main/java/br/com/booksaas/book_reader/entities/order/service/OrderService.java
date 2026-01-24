package br.com.booksaas.book_reader.entities.order.service;

import br.com.booksaas.book_reader.entities.order.entity.Order;
import br.com.booksaas.book_reader.entities.user.entity.User;
import br.com.booksaas.book_reader.events.BeforeDeleteUser;
import br.com.booksaas.book_reader.entities.order.dto.OrderDTO;
import br.com.booksaas.book_reader.entities.order.repositorie.OrderRepository;
import br.com.booksaas.book_reader.entities.user.repositorie.UserRepository;
import br.com.booksaas.book_reader.util.NotFoundException;
import br.com.booksaas.book_reader.util.ReferencedException;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderService(
            final OrderRepository orderRepository,
            final UserRepository userRepository
    ) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    /* =======================
       PAGINAÇÃO
       ======================= */
    public Page<OrderDTO> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(order -> mapToDTO(order, new OrderDTO()));
    }

    /* =======================
       BUSCA POR ID
       ======================= */
    public OrderDTO get(final Long id) {
        return orderRepository.findById(id)
                .map(order -> mapToDTO(order, new OrderDTO()))
                .orElseThrow(NotFoundException::new);
    }

    /* =======================
       CREATE
       ======================= */
    public Long create(final OrderDTO orderDTO) {
        final Order order = new Order();
        mapToEntity(orderDTO, order);
        return orderRepository.save(order).getId();
    }

    /* =======================
       UPDATE
       ======================= */
    public void update(final Long id, final OrderDTO orderDTO) {
        final Order order = orderRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(orderDTO, order);
        orderRepository.save(order);
    }

    /* =======================
       DELETE
       ======================= */
    public void delete(final Long id) {
        final Order order = orderRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        orderRepository.delete(order);
    }

    /* =======================
       MAPEAMENTOS
       ======================= */
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

        final User user = orderDTO.getUser() == null
                ? null
                : userRepository.findById(orderDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));

        order.setUser(user);
        return order;
    }

    /* =======================
       EVENTOS
       ======================= */
    @EventListener(BeforeDeleteUser.class)
    public void on(final BeforeDeleteUser event) {
        if (orderRepository.existsByUserId(event.getId())) {
            final ReferencedException ex = new ReferencedException();
            ex.setKey("user.order.user.referenced");
            ex.addParam(event.getId());
            throw ex;
        }
    }
}
