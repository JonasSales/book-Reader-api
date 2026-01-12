package br.com.booksaas.book_reader.user.service;

import br.com.booksaas.book_reader.role.entity.Role;
import br.com.booksaas.book_reader.events.BeforeDeleteRole;
import br.com.booksaas.book_reader.events.BeforeDeleteUser;
import br.com.booksaas.book_reader.role.repositorie.RoleRepository;
import br.com.booksaas.book_reader.user.entity.User;
import br.com.booksaas.book_reader.user.dto.UserDTO;
import br.com.booksaas.book_reader.user.repositorie.UserRepository;
import br.com.booksaas.book_reader.util.CustomCollectors;
import br.com.booksaas.book_reader.util.NotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ApplicationEventPublisher publisher;
    private final PasswordEncoder passwordEncoder;

    public UserService(final UserRepository userRepository, final RoleRepository roleRepository,
                       final ApplicationEventPublisher publisher, @Lazy final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.publisher = publisher;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Long id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UserDTO userDTO) {
        validateEmailUniqueness(userDTO.getEmail(), null);

        final User user = new User();
        mapToEntity(userDTO, user);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        Role roleUser = roleRepository.findAll().stream()
                .filter(r -> "ROLE_USER".equalsIgnoreCase(r.getName()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Default role 'ROLE_USER' not found"));
        user.setUserRoleRoles(new HashSet<>(List.of(roleUser)));
        return userRepository.save(user).getId();
    }

    public void update(final Long id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        validateEmailUniqueness(userDTO.getEmail(), id);

        mapToEntity(userDTO, user);
        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        userRepository.save(user);
    }

    public void delete(final Long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteUser(id));
        userRepository.delete(user);
    }

    private void validateEmailUniqueness(String email, Long idToIgnore) {
        userRepository.findByEmail(email).ifPresent(user -> {
            if (idToIgnore == null || !user.getId().equals(idToIgnore)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
            }
        });
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setIsPremium(user.getIsPremium());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUserRoleRoles(user.getUserRoleRoles().stream()
                .map(Role::getId)
                .toList());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setIsPremium(userDTO.getIsPremium());
        user.setCreatedAt(userDTO.getCreatedAt());

        final List<Role> userRoleRoles = roleRepository.findAllById(
                userDTO.getUserRoleRoles() == null ? List.of() : userDTO.getUserRoleRoles());
        if (userDTO.getUserRoleRoles() != null && !userDTO.getUserRoleRoles().isEmpty()) {
            if (userRoleRoles.size() != userDTO.getUserRoleRoles().size()) {
                throw new NotFoundException("one of userRoleRoles not found");
            }
        }

        user.setUserRoleRoles(new HashSet<>(userRoleRoles));
        return user;
    }

    public Map<Long, String> getUserValues() {
        return userRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getFullName));
    }

    @EventListener(BeforeDeleteRole.class)
    public void on(final BeforeDeleteRole event) {
        userRepository.findAllByUserRoleRolesId(event.getId()).forEach(user ->
                user.getUserRoleRoles().removeIf(role -> role.getId().equals(event.getId())));
    }
}