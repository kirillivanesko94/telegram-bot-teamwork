package pro.sky.telegrambotshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.sky.telegrambotshelter.entity.Shelter;
import pro.sky.telegrambotshelter.shelter.ShelterType;

import java.util.Collection;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {
}
