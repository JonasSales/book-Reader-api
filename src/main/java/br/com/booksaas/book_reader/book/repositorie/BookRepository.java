package br.com.booksaas.book_reader.book.repositorie;

import br.com.booksaas.book_reader.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRepository extends JpaRepository<Book, Long> {

    Book findFirstByUserId(Long id);

}
