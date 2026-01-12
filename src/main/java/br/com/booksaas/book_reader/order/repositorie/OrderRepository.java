package br.com.booksaas.book_reader.order.repositorie;

import br.com.booksaas.book_reader.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findFirstByUserId(Long id);

}
