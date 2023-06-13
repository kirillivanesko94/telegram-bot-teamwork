package pro.sky.telegrambotshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambotshelter.entity.Photo;

/**
 * Repository interface for photo
 * @autor Egor
 */
@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
