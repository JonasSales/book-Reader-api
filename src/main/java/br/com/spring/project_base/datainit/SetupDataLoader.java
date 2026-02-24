package br.com.spring.project_base.datainit;

import br.com.spring.project_base.entities.user.entity.Role;
import br.com.spring.project_base.entities.user.entity.User;
import br.com.spring.project_base.entities.user.repositorie.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.application.name}")
    private String projectName;

    public SetupDataLoader(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String @NonNull ... args) {
        // 1. Definir o e-mail dinamicamente usando o nome do projeto
        String adminEmail = "admin@" + projectName + ".com";

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