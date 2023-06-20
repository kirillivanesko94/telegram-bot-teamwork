package pro.sky.telegrambotshelter.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

/**
 * This class represents information about users,
 * including their name, email address, phone number, and chat ID.
 * It is used to work with user data,
 * including creating, reading, updating, and deleting users from the database.
 */
@Entity
@Data
public class Users {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String email;
    private String phone;
    private Long chatId;

    /** Поле коллекция отчетов пользователя */
    @OneToMany(mappedBy = "users")
    @JsonIgnore
    private Collection<Report> reports;

}
