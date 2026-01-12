package br.com.booksaas.book_reader.usersetting.controller;

import br.com.booksaas.book_reader.usersetting.dto.UserSettingDTO;
import br.com.booksaas.book_reader.usersetting.service.UserSettingService;
import br.com.booksaas.book_reader.user.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/userSettings", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserSettingResource {

    private final UserSettingService userSettingService;
    private final UserService userService;

    public UserSettingResource(final UserSettingService userSettingService,
            final UserService userService) {
        this.userSettingService = userSettingService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserSettingDTO>> getAllUserSettings() {
        return ResponseEntity.ok(userSettingService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserSettingDTO> getUserSetting(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(userSettingService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createUserSetting(
            @RequestBody @Valid final UserSettingDTO userSettingDTO) {
        final Long createdId = userSettingService.create(userSettingDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateUserSetting(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final UserSettingDTO userSettingDTO) {
        userSettingService.update(id, userSettingDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteUserSetting(@PathVariable(name = "id") final Long id) {
        userSettingService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/userValues")
    public ResponseEntity<Map<Long, String>> getUserValues() {
        return ResponseEntity.ok(userService.getUserValues());
    }

}
