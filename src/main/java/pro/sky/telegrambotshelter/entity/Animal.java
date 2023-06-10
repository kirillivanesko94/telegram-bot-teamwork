package pro.sky.telegrambotshelter.entity;

import lombok.Data;

import javax.persistence.*;

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
