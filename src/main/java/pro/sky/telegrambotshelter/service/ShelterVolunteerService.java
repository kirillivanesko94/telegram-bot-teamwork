package pro.sky.telegrambotshelter.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambotshelter.repository.UserRepository;

import pro.sky.telegrambotshelter.shelter.ShelterVolunteerType;

@Service

public class ShelterVolunteerService {
    private final UserRepository userRepository;
    public ShelterVolunteerService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public String getInfoAboutQuestion(ShelterVolunteerType type) {

        return "ВПожалуйста укажите ваш контактный номер телефона, в ближайшее время с вами свяжется волонтер, чтоб решить ваш вопрос. (заглушка)";
    }
}
