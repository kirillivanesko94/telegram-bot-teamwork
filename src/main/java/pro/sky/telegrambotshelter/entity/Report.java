package pro.sky.telegrambotshelter.entity;

import lombok.Data;
import pro.sky.telegrambotshelter.entity.Photo;

import javax.persistence.*;

/**
 * Класс сущности Отчет
 * @autor Егор
 */
@Entity
@Data
public class Report {
    /** Поле id */
    @Id
    @GeneratedValue
    private Long id;

    /** Поле пользователь */
    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;

    /** Поле текст отчета */
    private String reportText;

}
