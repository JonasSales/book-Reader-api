package br.com.booksaas.book_reader.user.repositorie;

import java.util.List;
import java.util.Optional;

import br.com.booksaas.book_reader.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);

}
