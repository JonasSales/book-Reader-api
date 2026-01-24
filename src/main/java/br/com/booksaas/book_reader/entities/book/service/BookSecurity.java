package br.com.booksaas.book_reader.entities.book.service;

import br.com.booksaas.book_reader.entities.book.repositorie.BookRepository;
import br.com.booksaas.book_reader.entities.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class BookSecurity {

    private final BookRepository bookRepository;

    public BookSecurity(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public boolean isOwner(Long bookId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        assert user != null;
        return bookRepository.existsByIdAndUserId(bookId, user.getId());
    }
}
