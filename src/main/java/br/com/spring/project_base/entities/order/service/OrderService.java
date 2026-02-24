package br.com.spring.project_base.entities.order.service;

import br.com.spring.project_base.entities.order.entity.Order;
import br.com.spring.project_base.entities.order.entity.OrderStatus;
import br.com.spring.project_base.entities.user.entity.User;
import br.com.spring.project_base.entities.user.service.UserService;
import br.com.spring.project_base.entities.order.dto.OrderDTO;
import br.com.spring.project_base.entities.order.repositorie.OrderRepository;

import br.com.spring.project_base.util.NotFoundException;
import br.com.spring.project_base.util.ReferencedException;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    public OrderService(
            final OrderRepository orderRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
    }

    /* =======================
       PAGINAÇÃO
       ======================= */
    public Page<OrderDTO> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(order -> mapToDTO(order, new OrderDTO()));
    }


    public Page<OrderDTO> findAllOrdersByUser(Pageable pageable, Long idUser){
        return orderRepository.findAllByUserId(pageable, idUser)
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
    public Long create(User user,final OrderDTO orderDTO) {
        Order order = new Order();
        order.setStatus(OrderStatus.SENT);
        order.setUser(user);
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
       Cancelando Pedido
       ======================= */
    public void cancelOrder(Long idOrder) {
        Order order = orderRepository.findById(idOrder).orElseThrow(NotFoundException::new);
        order.setStatus(OrderStatus.CANCELED);
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

    private void mapToEntity(final OrderDTO orderDTO, final Order order) {
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setStatus(orderDTO.getStatus());
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setTransactionId(orderDTO.getTransactionId());
        final User user = new User(userService.get((orderDTO.getUser())));
        order.setUser(user);
    }

}
