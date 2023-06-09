package pro.sky.telegrambotshelter.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambotshelter.repository.ShelterRepository;
import pro.sky.telegrambotshelter.shelter.ShelterType;

@Service
public class ShelterService {
    private final ShelterRepository shelterRepository;

    public ShelterService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    public String getInfo(ShelterType type) {
        // Инфу надо доставать из БД
        // сейчас просто заглушка
        return "Тут пока что пусто :(";
    }
}
