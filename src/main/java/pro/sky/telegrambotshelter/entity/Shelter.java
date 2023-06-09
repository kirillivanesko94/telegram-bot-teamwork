package pro.sky.telegrambotshelter.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Shelter {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String address;
    private String description;

}