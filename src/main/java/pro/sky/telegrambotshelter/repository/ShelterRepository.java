package pro.sky.telegrambotshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambotshelter.entity.Animal;
import pro.sky.telegrambotshelter.entity.Shelter;
import pro.sky.telegrambotshelter.shelter.ShelterType;

import java.util.Optional;

/**
 * This ShelterRepository interface represents a repository for accessing
 * and managing data of objects of the {@link Shelter} class.
 * It extends the JpaRepository interface,
 * which provides basic operations for working with entities in the database,
 * such as creating, reading, updating and deleting.
 */
public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    Optional<Shelter> findFirstByTypeAnimal(ShelterType type);
}
