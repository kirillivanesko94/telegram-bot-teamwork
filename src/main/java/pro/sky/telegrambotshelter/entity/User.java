package pro.sky.telegrambotshelter.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Collection;

/**
 * This class represents information about users,
 * including their name, email address, phone number, and chat ID.
 * It is used to work with user data,
 * including creating, reading, updating, and deleting users from the database.
 */
@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String email;
    private String phone;
    private Long chatId;

    /** Поле коллекция отчетов пользователя */
    @OneToMany(mappedBy = "user")
    private Collection<Report> reports;

}