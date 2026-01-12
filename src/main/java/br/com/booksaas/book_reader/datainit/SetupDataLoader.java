package br.com.booksaas.book_reader.datainit;

import br.com.booksaas.book_reader.role.entity.Role;
import br.com.booksaas.book_reader.user.entity.User;
import br.com.booksaas.book_reader.role.repositorie.RoleRepository;
import br.com.booksaas.book_reader.user.repositorie.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class SetupDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public SetupDataLoader(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 1. Criar as roles com prefixo ROLE_
        Role roleAdmin = createRoleIfNotFound("ROLE_ADMIN");
        createRoleIfNotFound("ROLE_USER");
        createRoleIfNotFound("ROLE_PREMIUM_USER");

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

            Set<Role> roles = new HashSet<>();
            roles.add(roleAdmin);
            admin.setUserRoleRoles(roles);

            userRepository.save(admin);
            System.out.println("Usuário Admin criado: " + adminEmail);
        }
    }

    private Role createRoleIfNotFound(String name) {
        return roleRepository.findAll().stream()
                .filter(role -> role.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(name);
                    return roleRepository.save(role);
                });
    }
}