package br.com.booksaas.book_reader.book.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BookDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String title;

    @NotNull
    @Size(max = 255)
    private String author;

    private String description;

    private String coverUrl;

    @NotNull
    private String fileUrl;

    @NotNull
    @Size(max = 255)
    private String fileName;

    @NotNull
    @Size(max = 255)
    private String fileType;

    @NotNull
    @JsonProperty("isPremium")
    private Boolean isPremium;

    @NotNull
    private OffsetDateTime uploadedAt;

    @NotNull
    private Long user;

}
