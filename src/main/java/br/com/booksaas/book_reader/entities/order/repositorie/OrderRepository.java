package br.com.booksaas.book_reader.entities.order.repositorie;

import br.com.booksaas.book_reader.entities.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByUserId(Long userId);

}
