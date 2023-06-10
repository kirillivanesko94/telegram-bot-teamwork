package pro.sky.telegrambotshelter.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@ToString
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
