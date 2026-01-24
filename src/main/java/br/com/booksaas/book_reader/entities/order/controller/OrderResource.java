package br.com.booksaas.book_reader.entities.order.controller;

import br.com.booksaas.book_reader.entities.order.dto.OrderDTO;
import br.com.booksaas.book_reader.entities.order.service.OrderService;
import br.com.booksaas.book_reader.entities.user.service.UserService;
import jakarta.validation.Valid;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderResource {

    private final OrderService orderService;
    private final UserService userService;

    public OrderResource(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping
    public Page<OrderDTO> getAllOrders(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return orderService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public OrderDTO getOrder(@PathVariable Long id) {
        return orderService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createOrder(@RequestBody @Valid OrderDTO dto) {
        return orderService.create(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOrder(@PathVariable Long id,
                            @RequestBody @Valid OrderDTO dto) {
        orderService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long id) {
        orderService.delete(id);
    }

    /* =======================
       USO INTERNO / LOOKUPS
       ======================= */
    @GetMapping("/users/values")
    public Map<Long, String> getUserValues() {
        return userService.getUserValues();
    }
}
