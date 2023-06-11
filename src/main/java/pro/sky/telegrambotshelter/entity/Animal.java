package pro.sky.telegrambotshelter.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * This class provides information about the animal,
 * including its type, description and instructions,
 * as well as the connection with the shelter in which the animal is located.
 * It is used to work with animal data,
 * including creating, reading, updating and deleting animals from the database,
 * as well as to establish communication with the appropriate shelter.
 */
@Entity
@Data
public class Animal {
    @Id
    @GeneratedValue
    private Long id;

    private String typeAnimal;
    private String descAnimal;
    private String instruction;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

}
