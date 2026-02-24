package br.com.spring.project_base.entities.user.repositorie;

import java.util.Optional;

import br.com.spring.project_base.entities.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);

}
