package br.com.booksaas.book_reader.events;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class BeforeDeleteBook {

    private Long id;

}
