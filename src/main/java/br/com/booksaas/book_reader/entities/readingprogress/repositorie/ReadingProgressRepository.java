package br.com.booksaas.book_reader.entities.readingprogress.repositorie;

import br.com.booksaas.book_reader.entities.readingprogress.entity.ReadingProgress;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReadingProgressRepository extends JpaRepository<ReadingProgress, Long> {

    ReadingProgress findFirstByUserId(Long id);

    ReadingProgress findFirstByBookId(Long id);

}
