package br.com.booksaas.book_reader.readingprogress.controller;

import br.com.booksaas.book_reader.readingprogress.dto.ReadingProgressDTO;
import br.com.booksaas.book_reader.book.service.BookService;
import br.com.booksaas.book_reader.readingprogress.service.ReadingProgressService;
import br.com.booksaas.book_reader.user.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/readingProgresses", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReadingProgressResource {

    private final ReadingProgressService readingProgressService;
    private final UserService userService;
    private final BookService bookService;

    public ReadingProgressResource(final ReadingProgressService readingProgressService,
            final UserService userService, final BookService bookService) {
        this.readingProgressService = readingProgressService;
        this.userService = userService;
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<ReadingProgressDTO>> getAllReadingProgresses() {
        return ResponseEntity.ok(readingProgressService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadingProgressDTO> getReadingProgress(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(readingProgressService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createReadingProgress(
            @RequestBody @Valid final ReadingProgressDTO readingProgressDTO) {
        final Long createdId = readingProgressService.create(readingProgressDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateReadingProgress(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ReadingProgressDTO readingProgressDTO) {
        readingProgressService.update(id, readingProgressDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteReadingProgress(@PathVariable(name = "id") final Long id) {
        readingProgressService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/userValues")
    public ResponseEntity<Map<Long, String>> getUserValues() {
        return ResponseEntity.ok(userService.getUserValues());
    }

    @GetMapping("/bookValues")
    public ResponseEntity<Map<Long, String>> getBookValues() {
        return ResponseEntity.ok(bookService.getBookValues());
    }

}
