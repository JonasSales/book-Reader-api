package br.com.booksaas.book_reader.book.controller;

import br.com.booksaas.book_reader.book.dto.BookDTO;
import br.com.booksaas.book_reader.book.service.BookService;
import br.com.booksaas.book_reader.user.service.UserService;
import jakarta.validation.Valid;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/books", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookResource {

    private final BookService bookService;
    private final UserService userService;

    public BookResource(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping
    public Page<BookDTO> getAllBooks(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public BookDTO getBook(@PathVariable Long id) {
        return bookService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createBook(@RequestBody @Valid BookDTO bookDTO) {
        return bookService.create(bookDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBook(
            @PathVariable Long id,
            @RequestBody @Valid BookDTO bookDTO
    ) {
        bookService.update(id, bookDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }

    /* =======================
       USO INTERNO / LOOKUPS
       ======================= */
    @GetMapping("/users/values")
    public Map<Long, String> getUserValues() {
        return userService.getUserValues();
    }
}
