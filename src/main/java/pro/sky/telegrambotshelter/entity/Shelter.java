package pro.sky.telegrambotshelter.entity;

import lombok.Data;
import pro.sky.telegrambotshelter.shelter.ShelterType;

import javax.persistence.*;
import java.util.Collection;


/**
 * This class represents basic information about the shelter
 * and its relationship to the collection of Animal objects that are in this shelter.
 * It is used to work with shelter data,
 * including creating, reading, updating and deleting shelters from the database.
 */
@Entity
@Data
public class Shelter {
    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private ShelterType type;
    private String name;
    private String description;
    private String instruction;


}
