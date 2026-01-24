package br.com.booksaas.book_reader.entities.usersetting.controller;

import br.com.booksaas.book_reader.entities.usersetting.dto.UserSettingDTO;
import br.com.booksaas.book_reader.entities.usersetting.service.UserSettingService;
import br.com.booksaas.book_reader.entities.user.service.UserService;
import jakarta.validation.Valid;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/user-settings", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserSettingResource {

    private final UserSettingService userSettingService;
    private final UserService userService;

    public UserSettingResource(UserSettingService userSettingService,
                               UserService userService) {
        this.userSettingService = userSettingService;
        this.userService = userService;
    }

    @GetMapping
    public Page<UserSettingDTO> getAllUserSettings(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return userSettingService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public UserSettingDTO getUserSetting(@PathVariable Long id) {
        return userSettingService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createUserSetting(@RequestBody @Valid UserSettingDTO dto) {
        return userSettingService.create(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserSetting(
            @PathVariable Long id,
            @RequestBody @Valid UserSettingDTO dto
    ) {
        userSettingService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserSetting(@PathVariable Long id) {
        userSettingService.delete(id);
    }

    /* =======================
       USO INTERNO / LOOKUPS
       ======================= */
    @GetMapping("/users/values")
    public Map<Long, String> getUserValues() {
        return userService.getUserValues();
    }
}
