package br.com.booksaas.book_reader.entities.order.controller;

import br.com.booksaas.book_reader.entities.order.dto.OrderDTO;
import br.com.booksaas.book_reader.entities.order.service.OrderService;
import br.com.booksaas.book_reader.entities.user.entity.User;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderResource {

    private final OrderService orderService;

    public OrderResource(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<OrderDTO> getAllOrders(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return orderService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderDTO getOrderUser(@PathVariable Long id){
        return orderService.get(id);
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("@orderSecurity.isOwner(#id, authentication)")
    public OrderDTO getOrder(@PathVariable Long id) {
        return orderService.get(id);
    }

    @GetMapping("/me")
    public Page<OrderDTO> getOrdersUser(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @AuthenticationPrincipal User user){
        return orderService.findAllOrdersByUser(pageable, user.getId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createOrder(@AuthenticationPrincipal User user, @RequestBody @Valid OrderDTO dto) {
        return orderService.create(user,dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@orderSecurity.isOwner(#id, authentication)")
    public void updateOrder(@PathVariable Long id,
                            @RequestBody @Valid OrderDTO dto) {
        orderService.update(id, dto);
    }

    @PutMapping("/cancel/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@orderSecurity.isOwner(#id, authentication)")
    public void cancelOrder(@PathVariable Long id){
        orderService.cancelOrder(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@orderSecurity.isOwner(#id, authentication)")
    public void deleteOrder(@PathVariable Long id) {
        orderService.delete(id);
    }

}
