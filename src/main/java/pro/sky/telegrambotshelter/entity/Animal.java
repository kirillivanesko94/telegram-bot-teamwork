package pro.sky.telegrambotshelter.entity;

import lombok.Data;
import lombok.ToString;
import pro.sky.telegrambotshelter.shelter.ShelterType;

import javax.persistence.*;

@Entity
@Data
public class Animal {
    @Id
    @GeneratedValue
    private Long id;

    private ShelterType typeAnimal;
    private String descAnimal;
    private String instruction;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

}
