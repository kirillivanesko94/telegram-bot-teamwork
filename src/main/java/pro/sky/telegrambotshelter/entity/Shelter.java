package pro.sky.telegrambotshelter.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Collection;

@Entity
@Data
@ToString
public class Shelter {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "shelter")
    private Collection<Animal> animals;

    private String name;
    private String description;

}
