package pro.sky.telegrambotshelter.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambotshelter.repository.UsersRepository;

import pro.sky.telegrambotshelter.shelter.ShelterVolunteerType;

@Service

public class ShelterVolunteerService {
    private final UsersRepository usersRepository;
    public ShelterVolunteerService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
    public String getInfoAboutQuestion(ShelterVolunteerType type) {
        return "Пожалуйста укажите ваш контактный номер телефона, в ближайшее время с вами свяжется волонтер, чтоб решить ваш вопрос. (заглушка)";
    }
}
