package br.com.booksaas.book_reader.book.service;

import br.com.booksaas.book_reader.book.entity.Book;
import br.com.booksaas.book_reader.user.entity.User;
import br.com.booksaas.book_reader.events.BeforeDeleteBook;
import br.com.booksaas.book_reader.events.BeforeDeleteUser;
import br.com.booksaas.book_reader.book.dto.BookDTO;
import br.com.booksaas.book_reader.book.repositorie.BookRepository;
import br.com.booksaas.book_reader.user.repositorie.UserRepository;
import br.com.booksaas.book_reader.util.CustomCollectors;
import br.com.booksaas.book_reader.util.NotFoundException;
import br.com.booksaas.book_reader.util.ReferencedException;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher publisher;

    public BookService(final BookRepository bookRepository, final UserRepository userRepository,
            final ApplicationEventPublisher publisher) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.publisher = publisher;
    }

    public List<BookDTO> findAll() {
        final List<Book> books = bookRepository.findAll(Sort.by("id"));
        return books.stream()
                .map(book -> mapToDTO(book, new BookDTO()))
                .toList();
    }

    public BookDTO get(final Long id) {
        return bookRepository.findById(id)
                .map(book -> mapToDTO(book, new BookDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final BookDTO bookDTO) {
        final Book book = new Book();
        mapToEntity(bookDTO, book);
        return bookRepository.save(book).getId();
    }

    public void update(final Long id, final BookDTO bookDTO) {
        final Book book = bookRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(bookDTO, book);
        bookRepository.save(book);
    }

    public void delete(final Long id) {
        final Book book = bookRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteBook(id));
        bookRepository.delete(book);
    }

    private BookDTO mapToDTO(final Book book, final BookDTO bookDTO) {
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setDescription(book.getDescription());
        bookDTO.setCoverUrl(book.getCoverUrl());
        bookDTO.setFileUrl(book.getFileUrl());
        bookDTO.setFileName(book.getFileName());
        bookDTO.setFileType(book.getFileType());
        bookDTO.setIsPremium(book.getIsPremium());
        bookDTO.setUploadedAt(book.getUploadedAt());
        bookDTO.setUser(book.getUser() == null ? null : book.getUser().getId());
        return bookDTO;
    }

    private Book mapToEntity(final BookDTO bookDTO, final Book book) {
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setDescription(bookDTO.getDescription());
        book.setCoverUrl(bookDTO.getCoverUrl());
        book.setFileUrl(bookDTO.getFileUrl());
        book.setFileName(bookDTO.getFileName());
        book.setFileType(bookDTO.getFileType());
        book.setIsPremium(bookDTO.getIsPremium());
        book.setUploadedAt(bookDTO.getUploadedAt());
        final User user = bookDTO.getUser() == null ? null : userRepository.findById(bookDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        book.setUser(user);
        return book;
    }

    public Map<Long, String> getBookValues() {
        return bookRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Book::getId, Book::getTitle));
    }

    @EventListener(BeforeDeleteUser.class)
    public void on(final BeforeDeleteUser event) {
        final ReferencedException referencedException = new ReferencedException();
        final Book userBook = bookRepository.findFirstByUserId(event.getId());
        if (userBook != null) {
            referencedException.setKey("user.book.user.referenced");
            referencedException.addParam(userBook.getId());
            throw referencedException;
        }
    }

}
