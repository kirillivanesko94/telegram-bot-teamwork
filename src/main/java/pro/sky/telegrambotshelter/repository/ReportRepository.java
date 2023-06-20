package pro.sky.telegrambotshelter.repository;

import com.pengrad.telegrambot.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambotshelter.entity.Report;
import pro.sky.telegrambotshelter.entity.Users;

import java.util.List;
import java.util.Map;

/**
 * Repository interface for report
 *
 * @autor Egor
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByUsers(Users users);
}
