package br.com.booksaas.book_reader.usersetting.repositorie;

import br.com.booksaas.book_reader.usersetting.entity.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserSettingRepository extends JpaRepository<UserSetting, Long> {

    boolean existsByUserId(Long userId);
}
