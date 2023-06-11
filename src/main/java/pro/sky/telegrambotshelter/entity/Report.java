package pro.sky.telegrambotshelter.entity;

import lombok.Data;
import pro.sky.telegrambotshelter.entity.Photo;

import javax.persistence.*;

@Entity
@Data
public class Report {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;

    private String reportText;

}
