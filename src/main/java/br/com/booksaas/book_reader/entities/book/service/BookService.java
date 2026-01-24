package br.com.booksaas.book_reader.entities.book.service;

import br.com.booksaas.book_reader.entities.book.entity.Book;
import br.com.booksaas.book_reader.entities.user.entity.User;
import br.com.booksaas.book_reader.events.BeforeDeleteBook;
import br.com.booksaas.book_reader.events.BeforeDeleteUser;
import br.com.booksaas.book_reader.entities.book.dto.BookDTO;
import br.com.booksaas.book_reader.entities.book.repositorie.BookRepository;
import br.com.booksaas.book_reader.entities.user.repositorie.UserRepository;
import br.com.booksaas.book_reader.util.CustomCollectors;
import br.com.booksaas.book_reader.util.NotFoundException;
import br.com.booksaas.book_reader.util.ReferencedException;
import java.util.Map;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher publisher;

    public BookService(
            final BookRepository bookRepository,
            final UserRepository userRepository,
            final ApplicationEventPublisher publisher
    ) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.publisher = publisher;
    }

    /* =======================
       PAGINAÇÃO
       ======================= */
    public Page<BookDTO> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(book -> mapToDTO(book, new BookDTO()));
    }

    /* =======================
       BUSCA POR ID
       ======================= */
    public BookDTO get(final Long id) {
        return bookRepository.findById(id)
                .map(book -> mapToDTO(book, new BookDTO()))
                .orElseThrow(NotFoundException::new);
    }

    /* =======================
       CREATE
       ======================= */
    public Long create(final BookDTO bookDTO) {
        final Book book = new Book();
        mapToEntity(bookDTO, book);
        return bookRepository.save(book).getId();
    }

    /* =======================
       UPDATE
       ======================= */
    public void update(final Long id, final BookDTO bookDTO) {
        final Book book = bookRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(bookDTO, book);
        bookRepository.save(book);
    }

    /* =======================
       DELETE
       ======================= */
    public void delete(final Long id) {
        final Book book = bookRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteBook(id));
        bookRepository.delete(book);
    }

    /* =======================
       MAPEAMENTOS
       ======================= */
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

    private void mapToEntity(final BookDTO bookDTO, final Book book) {
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setDescription(bookDTO.getDescription());
        book.setCoverUrl(bookDTO.getCoverUrl());
        book.setFileUrl(bookDTO.getFileUrl());
        book.setFileName(bookDTO.getFileName());
        book.setFileType(bookDTO.getFileType());
        book.setIsPremium(bookDTO.getIsPremium());
        book.setUploadedAt(bookDTO.getUploadedAt());

        final User user = bookDTO.getUser() == null
                ? null
                : userRepository.findById(bookDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));

        book.setUser(user);
    }

    /* =======================
       USO INTERNO (MAP)
       ======================= */
    public Map<Long, String> getBookValues() {
        return bookRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Book::getId, Book::getTitle));
    }

    /* =======================
       EVENTOS
       ======================= */
    @EventListener(BeforeDeleteUser.class)
    public void on(final BeforeDeleteUser event) {
        if (bookRepository.existsByUserId(event.getId())) {
            final ReferencedException ex = new ReferencedException();
            ex.setKey("user.book.user.referenced");
            ex.addParam(event.getId());
            throw ex;
        }
    }
}
