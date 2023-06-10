package pro.sky.telegrambotshelter.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@ToString
public class Users {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String email;
    private int phone;
    private Long chatId;
    private int shelterId;

}
