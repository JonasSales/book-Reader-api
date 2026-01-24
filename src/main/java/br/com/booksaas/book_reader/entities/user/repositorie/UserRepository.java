package br.com.booksaas.book_reader.entities.user.repositorie;

import java.util.Optional;

import br.com.booksaas.book_reader.entities.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);

}
