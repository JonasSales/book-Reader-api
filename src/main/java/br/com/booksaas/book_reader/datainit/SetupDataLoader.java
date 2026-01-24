package br.com.booksaas.book_reader.datainit;

import br.com.booksaas.book_reader.entities.user.entity.Role;
import br.com.booksaas.book_reader.entities.user.entity.User;
import br.com.booksaas.book_reader.entities.user.repositorie.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

@Component
public class SetupDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SetupDataLoader(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String @NonNull ... args) {
        // 1. Criar as roles com prefixo ROLE_

        // 2. Criar o usuário Admin
        String adminEmail = "admin@bookreader.com";
        Optional<User> adminOptional = userRepository.findByEmail(adminEmail);

        if (adminOptional.isEmpty()) {
            User admin = new User();
            admin.setFullName("Administrador do Sistema");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setIsPremium(true);
            admin.setCreatedAt(OffsetDateTime.now());
            admin.setRole(Role.ROLE_ADMIN);

            userRepository.save(admin);
            System.out.println("Usuário Admin criado: " + adminEmail);
        }
    }

}