package br.com.booksaas.book_reader.role.repositorie;

import br.com.booksaas.book_reader.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {
}
