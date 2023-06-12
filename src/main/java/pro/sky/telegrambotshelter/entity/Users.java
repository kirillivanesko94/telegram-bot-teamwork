package pro.sky.telegrambotshelter.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Collection;

@Entity
@Data
public class Users {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String email;
    private int phone;
    private Long chatId;

    /** Поле коллекция отчетов пользователя */
    @OneToMany(mappedBy = "users")
    private Collection<Report> reports;

}
