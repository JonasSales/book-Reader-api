package br.com.spring.project_base.security.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    @NotNull
    private String email;

    @NotNull
    private String password;
}