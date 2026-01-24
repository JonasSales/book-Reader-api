package br.com.booksaas.book_reader.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;
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

    private List<Long> userRoles;

}
