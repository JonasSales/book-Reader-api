package br.com.booksaas.book_reader.entities.usersetting.repositorie;

import br.com.booksaas.book_reader.entities.usersetting.entity.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserSettingRepository extends JpaRepository<UserSetting, Long> {

    boolean existsByUserId(Long userId);
}
