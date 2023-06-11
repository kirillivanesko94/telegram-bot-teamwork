package pro.sky.telegrambotshelter.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
    private int phone;
    private Long chatId;

}
