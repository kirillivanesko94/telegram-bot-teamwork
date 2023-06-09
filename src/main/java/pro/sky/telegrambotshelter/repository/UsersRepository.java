package pro.sky.telegrambotshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambotshelter.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
}
