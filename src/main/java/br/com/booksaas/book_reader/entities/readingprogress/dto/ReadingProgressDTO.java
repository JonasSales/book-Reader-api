package br.com.booksaas.book_reader.entities.readingprogress.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReadingProgressDTO {

    private Long id;

    private Integer currentPage;

    private Double percentageCompleted;

    @Size(max = 255)
    private String lastCfiRange;

    @Size(max = 100)
    private String deviceType;

    private OffsetDateTime lastReadAt;

    @NotNull
    private Long user;

    @NotNull
    private Long book;

}
