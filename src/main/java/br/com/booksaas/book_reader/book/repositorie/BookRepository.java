package br.com.booksaas.book_reader.book.repositorie;

import br.com.booksaas.book_reader.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findByUserId(Long userId, Pageable pageable);

    boolean existsByUserId(Long userId);
}
