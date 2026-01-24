package br.com.booksaas.book_reader.entities.usersetting.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserSettingDTO {

    private Long id;

    @Size(max = 50)
    private String theme;

    private Integer fontSize;

    @Size(max = 100)
    private String fontFamily;

    @NotNull
    private Long user;

}
