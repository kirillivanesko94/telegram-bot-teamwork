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

    /** Field user */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /** Field report text */
    private String reportText;

    private File file;

}
