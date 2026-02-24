package br.com.spring.project_base.entities.user.service;
import br.com.spring.project_base.entities.user.entity.Role;
import br.com.spring.project_base.entities.user.entity.User;
import br.com.spring.project_base.entities.user.dto.UserDTO;
import br.com.spring.project_base.entities.user.repositorie.UserRepository;
import br.com.spring.project_base.util.NotFoundException;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final PasswordEncoder passwordEncoder;

    public UserService(
            final UserRepository userRepository,
            @Lazy final PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /* =======================
       SPRING SECURITY
       ======================= */
    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + email));
    }

    /* =======================
       PAGINAÇÃO
       ======================= */
    public Page<UserDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> mapToDTO(user, new UserDTO()));
    }

    /* =======================
       BUSCA POR ID
       ======================= */
    public UserDTO get(final Long id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    /* =======================
       CREATE
       ======================= */
    public Long create(final UserDTO userDTO) {
        validateEmailUniqueness(userDTO.getEmail(), null);

        final User user = new User();
        mapToEntity(userDTO, user);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(Role.ROLE_USER);
        return userRepository.save(user).getId();
    }

    /* =======================
       UPDATE
       ======================= */
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

    /* =======================
       DELETE
       ======================= */
    public void delete(final Long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        userRepository.delete(user);
    }

    /* =======================
       VALIDAÇÕES
       ======================= */
    private void validateEmailUniqueness(String email, Long idToIgnore) {
        userRepository.findByEmail(email).ifPresent(user -> {
            if (!user.getId().equals(idToIgnore)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Email already in use"
                );
            }
        });
    }

    /* =======================
       MAPEAMENTOS
       ======================= */
    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setIsPremium(user.getIsPremium());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    private void mapToEntity(final UserDTO userDTO, final User user) {
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setIsPremium(userDTO.getIsPremium());
    }
}
