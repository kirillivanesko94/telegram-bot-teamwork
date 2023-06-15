package pro.sky.telegrambotshelter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotshelter.entity.Shelter;
import pro.sky.telegrambotshelter.repository.ShelterRepository;
import pro.sky.telegrambotshelter.shelter.ShelterType;

import java.util.Optional;

/**
 * This class implements working with a database to receive or receive the necessary information
 */
@Slf4j
@Service
public class ShelterService {
    private final ShelterRepository shelterRepository;

    public ShelterService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    /**
     * Method for obtaining information about the nursery, depending on the selected type
     * @param type - Shelter type
     *
     */
    public String getInfo(ShelterType type) {
        Optional<Shelter> shelter = shelterRepository.findFirstByType(type);
        if (shelter.isPresent()){
            return shelter.get().getDescription();
        }
        log.info("в базе данных нет питомников с таким животным");
        return "Тут пока что пусто :(";
    }
}
