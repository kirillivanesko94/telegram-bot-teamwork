package pro.sky.telegrambotshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambotshelter.entity.Animal;

/**
 * This AnimalRepository interface represents a repository for accessing
 * and managing data of objects of the {@link Animal} class.
 * It extends the JpaRepository interface,
 * which provides basic operations for working with entities in the database,
 * such as creating, reading, updating and deleting.
 */
public interface AnimalRepository extends JpaRepository<Animal, Long> {
}
