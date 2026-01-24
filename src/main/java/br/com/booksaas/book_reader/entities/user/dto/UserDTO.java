package br.com.booksaas.book_reader.entities.user.dto;

import br.com.booksaas.book_reader.entities.user.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String fullName;

    @NotNull
    @Size(max = 255)
    @Email
    private String email;

    @NotNull
    @Size(max = 255)
    private String password;

    @NotNull
    @JsonProperty("isPremium")
    private Boolean isPremium;

    private OffsetDateTime createdAt;

    private Role role;

}
