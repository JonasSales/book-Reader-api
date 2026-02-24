package br.com.spring.project_base.entities.order.repositorie;

import br.com.spring.project_base.entities.order.entity.Order;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Override
    @Query("SELECT o FROM Order o JOIN FETCH o.user")
    Page<Order> findAll(@NonNull Pageable pageable);

    @Query("SELECT o FROM Order o JOIN FETCH o.user WHERE o.user.id = :userId")
    Page<Order> findAllByUserId(Pageable pageable, @Param("userId") Long userId);

    @Query("SELECT o FROM Order o JOIN FETCH o.user WHERE o.id = :id")
    Optional<Order> findById(@Param("id") Long id);

    boolean existsByUserId(Long userId);

    boolean existsByIdAndUserId(Long orderId, Long userId);
}