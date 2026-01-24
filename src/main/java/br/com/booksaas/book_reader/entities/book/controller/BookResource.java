package br.com.booksaas.book_reader.entities.book.controller;

import br.com.booksaas.book_reader.entities.book.dto.BookDTO;
import br.com.booksaas.book_reader.entities.book.service.BookService;
import br.com.booksaas.book_reader.entities.user.entity.User;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/books", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookResource {

    private final BookService bookService;

    public BookResource(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<BookDTO> getAllBooks(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/me")
    public Page<BookDTO> getBookByUser(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
       @AuthenticationPrincipal User user) {
        return bookService.findBookByUser(user.getId(), pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@bookSecurity.isOwner(#id, authentication)")
    public BookDTO getBook(@PathVariable Long id) {
        return bookService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createBook(@AuthenticationPrincipal User user, @RequestBody @Valid BookDTO bookDTO) {
        return bookService.create(user.getId(),bookDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@bookSecurity.isOwner(#id, authentication)")
    public void updateBook(@PathVariable Long id, @RequestBody @Valid BookDTO bookDTO) {
        bookService.update(id, bookDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@bookSecurity.isOwner(#id, authentication)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }
}
