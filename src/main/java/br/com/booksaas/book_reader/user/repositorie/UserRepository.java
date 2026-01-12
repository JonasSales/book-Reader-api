package br.com.booksaas.book_reader.user.repositorie;

import java.util.List;
import java.util.Optional;

import br.com.booksaas.book_reader.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userRoleRoles WHERE u.email = :email")
    Optional<User> findByEmailWithRoles(@Param("email") String email);

    List<User> findAllByUserRoleRolesId(Long id);

    Optional<User> findByEmail(String email);

}
