package pro.sky.telegrambotshelter.entity;

import com.pengrad.telegrambot.model.File;
import lombok.Data;

import javax.persistence.*;

/**
 * Entity class for report
 * @autor Egor
 */
@Entity
@Data
public class Report {
    /** Field identifier */
    @Id
    @GeneratedValue
    private Long id;

    /** Field users */
    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;

    /** Field report text */
    private String reportText;

    private File file;

}
