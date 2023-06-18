package pro.sky.telegrambotshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambotshelter.entity.Users;

/**
 * This UsersRepository interface represents a repository for accessing
 * and managing data of objects of the {@link Users} class.
 * It extends the JpaRepository interface,
 * which provides basic operations for working with entities in the database,
 * such as creating, reading, updating and deleting.
 */
public interface UsersRepository extends JpaRepository<Users, Long> {
}
