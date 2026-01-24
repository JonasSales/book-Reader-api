package br.com.booksaas.book_reader.readingprogress.service;

import br.com.booksaas.book_reader.book.entity.Book;
import br.com.booksaas.book_reader.readingprogress.entity.ReadingProgress;
import br.com.booksaas.book_reader.user.entity.User;
import br.com.booksaas.book_reader.events.BeforeDeleteBook;
import br.com.booksaas.book_reader.events.BeforeDeleteUser;
import br.com.booksaas.book_reader.readingprogress.dto.ReadingProgressDTO;
import br.com.booksaas.book_reader.book.repositorie.BookRepository;
import br.com.booksaas.book_reader.readingprogress.repositorie.ReadingProgressRepository;
import br.com.booksaas.book_reader.user.repositorie.UserRepository;
import br.com.booksaas.book_reader.util.NotFoundException;
import br.com.booksaas.book_reader.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ReadingProgressService {

    private final ReadingProgressRepository readingProgressRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public ReadingProgressService(final ReadingProgressRepository readingProgressRepository,
            final UserRepository userRepository, final BookRepository bookRepository) {
        this.readingProgressRepository = readingProgressRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public List<ReadingProgressDTO> findAll() {
        final List<ReadingProgress> readingProgresses = readingProgressRepository.findAll(Sort.by("id"));
        return readingProgresses.stream()
                .map(readingProgress -> mapToDTO(readingProgress, new ReadingProgressDTO()))
                .toList();
    }

    public ReadingProgressDTO get(final Long id) {
        return readingProgressRepository.findById(id)
                .map(readingProgress -> mapToDTO(readingProgress, new ReadingProgressDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ReadingProgressDTO readingProgressDTO) {
        final ReadingProgress readingProgress = new ReadingProgress();
        mapToEntity(readingProgressDTO, readingProgress);
        return readingProgressRepository.save(readingProgress).getId();
    }

    public void update(final Long id, final ReadingProgressDTO readingProgressDTO) {
        final ReadingProgress readingProgress = readingProgressRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(readingProgressDTO, readingProgress);
        readingProgressRepository.save(readingProgress);
    }

    public void delete(final Long id) {
        final ReadingProgress readingProgress = readingProgressRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        readingProgressRepository.delete(readingProgress);
    }

    private ReadingProgressDTO mapToDTO(final ReadingProgress readingProgress,
            final ReadingProgressDTO readingProgressDTO) {
        readingProgressDTO.setId(readingProgress.getId());
        readingProgressDTO.setCurrentPage(readingProgress.getCurrentPage());
        readingProgressDTO.setPercentageCompleted(readingProgress.getPercentageCompleted());
        readingProgressDTO.setLastCfiRange(readingProgress.getLastCfiRange());
        readingProgressDTO.setDeviceType(readingProgress.getDeviceType());
        readingProgressDTO.setLastReadAt(readingProgress.getLastReadAt());
        readingProgressDTO.setUser(readingProgress.getUser() == null ? null : readingProgress.getUser().getId());
        readingProgressDTO.setBook(readingProgress.getBook() == null ? null : readingProgress.getBook().getId());
        return readingProgressDTO;
    }

    private ReadingProgress mapToEntity(final ReadingProgressDTO readingProgressDTO,
            final ReadingProgress readingProgress) {
        readingProgress.setCurrentPage(readingProgressDTO.getCurrentPage());
        readingProgress.setPercentageCompleted(readingProgressDTO.getPercentageCompleted());
        readingProgress.setLastCfiRange(readingProgressDTO.getLastCfiRange());
        readingProgress.setDeviceType(readingProgressDTO.getDeviceType());
        readingProgress.setLastReadAt(readingProgressDTO.getLastReadAt());
        final User user = readingProgressDTO.getUser() == null ? null : userRepository.findById(readingProgressDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        readingProgress.setUser(user);
        final Book book = readingProgressDTO.getBook() == null ? null : bookRepository.findById(readingProgressDTO.getBook())
                .orElseThrow(() -> new NotFoundException("book not found"));
        readingProgress.setBook(book);
        return readingProgress;
    }

    @EventListener(BeforeDeleteUser.class)
    public void on(final BeforeDeleteUser event) {
        final ReferencedException referencedException = new ReferencedException();
        final ReadingProgress userReadingProgress = readingProgressRepository.findFirstByUserId(event.getId());
        if (userReadingProgress != null) {
            referencedException.setKey("user.readingProgress.user.referenced");
            referencedException.addParam(userReadingProgress.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteBook.class)
    public void on(final BeforeDeleteBook event) {
        final ReferencedException referencedException = new ReferencedException();
        final ReadingProgress bookReadingProgress = readingProgressRepository.findFirstByBookId(event.getId());
        if (bookReadingProgress != null) {
            referencedException.setKey("book.readingProgress.book.referenced");
            referencedException.addParam(bookReadingProgress.getId());
            throw referencedException;
        }
    }

}
