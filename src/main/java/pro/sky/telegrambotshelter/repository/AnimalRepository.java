package pro.sky.telegrambotshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambotshelter.entity.Animal;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
}
