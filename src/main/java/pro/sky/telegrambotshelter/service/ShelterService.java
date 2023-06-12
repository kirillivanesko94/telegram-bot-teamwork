package pro.sky.telegrambotshelter.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambotshelter.repository.ShelterRepository;
import pro.sky.telegrambotshelter.shelter.ShelterType;

/**
 * This class implements working with a database to receive or receive the necessary information
 */
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
        // Инфу надо доставать из БД
        // сейчас просто заглушка
        return "Тут пока что пусто :(";
    }
}
