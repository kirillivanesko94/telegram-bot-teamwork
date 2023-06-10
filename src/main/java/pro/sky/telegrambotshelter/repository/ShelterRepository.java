package pro.sky.telegrambotshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambotshelter.entity.Shelter;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {
}
