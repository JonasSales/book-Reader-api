package br.com.booksaas.book_reader.entities.usersetting.service;

import br.com.booksaas.book_reader.entities.usersetting.entity.UserSetting;
import br.com.booksaas.book_reader.entities.usersetting.dto.UserSettingDTO;
import br.com.booksaas.book_reader.entities.usersetting.repositorie.UserSettingRepository;
import br.com.booksaas.book_reader.entities.user.entity.User;
import br.com.booksaas.book_reader.events.BeforeDeleteUser;
import br.com.booksaas.book_reader.entities.user.repositorie.UserRepository;
import br.com.booksaas.book_reader.util.NotFoundException;
import br.com.booksaas.book_reader.util.ReferencedException;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class UserSettingService {

    private final UserSettingRepository userSettingRepository;
    private final UserRepository userRepository;

    public UserSettingService(
            final UserSettingRepository userSettingRepository,
            final UserRepository userRepository
    ) {
        this.userSettingRepository = userSettingRepository;
        this.userRepository = userRepository;
    }

    /* =======================
       PAGINAÇÃO
       ======================= */
    public Page<UserSettingDTO> findAll(Pageable pageable) {
        return userSettingRepository.findAll(pageable)
                .map(userSetting -> mapToDTO(userSetting, new UserSettingDTO()));
    }

    /* =======================
       BUSCA POR ID
       ======================= */
    public UserSettingDTO get(final Long id) {
        return userSettingRepository.findById(id)
                .map(userSetting -> mapToDTO(userSetting, new UserSettingDTO()))
                .orElseThrow(NotFoundException::new);
    }

    /* =======================
       CREATE
       ======================= */
    public Long create(final UserSettingDTO userSettingDTO) {
        final UserSetting userSetting = new UserSetting();
        mapToEntity(userSettingDTO, userSetting);
        return userSettingRepository.save(userSetting).getId();
    }

    /* =======================
       UPDATE
       ======================= */
    public void update(final Long id, final UserSettingDTO userSettingDTO) {
        final UserSetting userSetting = userSettingRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userSettingDTO, userSetting);
        userSettingRepository.save(userSetting);
    }

    /* =======================
       DELETE
       ======================= */
    public void delete(final Long id) {
        final UserSetting userSetting = userSettingRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        userSettingRepository.delete(userSetting);
    }

    /* =======================
       MAPEAMENTOS
       ======================= */
    private UserSettingDTO mapToDTO(
            final UserSetting userSetting,
            final UserSettingDTO userSettingDTO
    ) {
        userSettingDTO.setId(userSetting.getId());
        userSettingDTO.setTheme(userSetting.getTheme());
        userSettingDTO.setFontSize(userSetting.getFontSize());
        userSettingDTO.setFontFamily(userSetting.getFontFamily());
        userSettingDTO.setUser(
                userSetting.getUser() == null
                        ? null
                        : userSetting.getUser().getId()
        );
        return userSettingDTO;
    }

    private UserSetting mapToEntity(
            final UserSettingDTO userSettingDTO,
            final UserSetting userSetting
    ) {
        userSetting.setTheme(userSettingDTO.getTheme());
        userSetting.setFontSize(userSettingDTO.getFontSize());
        userSetting.setFontFamily(userSettingDTO.getFontFamily());

        final User user = userSettingDTO.getUser() == null
                ? null
                : userRepository.findById(userSettingDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));

        userSetting.setUser(user);
        return userSetting;
    }

    /* =======================
       EVENTOS
       ======================= */
    @EventListener(BeforeDeleteUser.class)
    public void on(final BeforeDeleteUser event) {
        if (userSettingRepository.existsByUserId(event.getId())) {
            final ReferencedException ex = new ReferencedException();
            ex.setKey("user.userSetting.user.referenced");
            ex.addParam(event.getId());
            throw ex;
        }
    }
}
