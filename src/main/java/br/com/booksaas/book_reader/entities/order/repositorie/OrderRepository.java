package br.com.booksaas.book_reader.entities.order.repositorie;

import br.com.booksaas.book_reader.entities.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByUserId(Long userId);

    boolean existsByIdAndUserId(Long bookId, Long id);

    Page<Order> findAllByUserId(Pageable pageable, Long userId);

    Long id(Long id);
}
