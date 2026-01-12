package br.com.booksaas.book_reader.usersetting.service;

import br.com.booksaas.book_reader.usersetting.entity.UserSetting;
import br.com.booksaas.book_reader.usersetting.dto.UserSettingDTO;
import br.com.booksaas.book_reader.usersetting.repositorie.UserSettingRepository;
import br.com.booksaas.book_reader.user.entity.User;
import br.com.booksaas.book_reader.events.BeforeDeleteUser;
import br.com.booksaas.book_reader.user.repositorie.UserRepository;
import br.com.booksaas.book_reader.util.NotFoundException;
import br.com.booksaas.book_reader.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserSettingService {

    private final UserSettingRepository userSettingRepository;
    private final UserRepository userRepository;

    public UserSettingService(final UserSettingRepository userSettingRepository,
            final UserRepository userRepository) {
        this.userSettingRepository = userSettingRepository;
        this.userRepository = userRepository;
    }

    public List<UserSettingDTO> findAll() {
        final List<UserSetting> userSettings = userSettingRepository.findAll(Sort.by("id"));
        return userSettings.stream()
                .map(userSetting -> mapToDTO(userSetting, new UserSettingDTO()))
                .toList();
    }

    public UserSettingDTO get(final Long id) {
        return userSettingRepository.findById(id)
                .map(userSetting -> mapToDTO(userSetting, new UserSettingDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UserSettingDTO userSettingDTO) {
        final UserSetting userSetting = new UserSetting();
        mapToEntity(userSettingDTO, userSetting);
        return userSettingRepository.save(userSetting).getId();
    }

    public void update(final Long id, final UserSettingDTO userSettingDTO) {
        final UserSetting userSetting = userSettingRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userSettingDTO, userSetting);
        userSettingRepository.save(userSetting);
    }

    public void delete(final Long id) {
        final UserSetting userSetting = userSettingRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        userSettingRepository.delete(userSetting);
    }

    private UserSettingDTO mapToDTO(final UserSetting userSetting,
            final UserSettingDTO userSettingDTO) {
        userSettingDTO.setId(userSetting.getId());
        userSettingDTO.setTheme(userSetting.getTheme());
        userSettingDTO.setFontSize(userSetting.getFontSize());
        userSettingDTO.setFontFamily(userSetting.getFontFamily());
        userSettingDTO.setUser(userSetting.getUser() == null ? null : userSetting.getUser().getId());
        return userSettingDTO;
    }

    private UserSetting mapToEntity(final UserSettingDTO userSettingDTO,
            final UserSetting userSetting) {
        userSetting.setTheme(userSettingDTO.getTheme());
        userSetting.setFontSize(userSettingDTO.getFontSize());
        userSetting.setFontFamily(userSettingDTO.getFontFamily());
        final User user = userSettingDTO.getUser() == null ? null : userRepository.findById(userSettingDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        userSetting.setUser(user);
        return userSetting;
    }

    @EventListener(BeforeDeleteUser.class)
    public void on(final BeforeDeleteUser event) {
        final ReferencedException referencedException = new ReferencedException();
        final UserSetting userUserSetting = userSettingRepository.findFirstByUserId(event.getId());
        if (userUserSetting != null) {
            referencedException.setKey("user.userSetting.user.referenced");
            referencedException.addParam(userUserSetting.getId());
            throw referencedException;
        }
    }

}
