package pro.sky.telegrambotshelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotshelter.entity.Users;
import pro.sky.telegrambotshelter.repository.UsersRepository;

import java.util.Collection;

@Service

public class ShelterVolunteerService {
    private final UsersRepository usersRepository;
    private final TelegramBot telegramBot;

    public ShelterVolunteerService(UsersRepository usersRepository, TelegramBot telegramBot) {
        this.usersRepository = usersRepository;
        this.telegramBot = telegramBot;
    }

    public String getInfoAboutQuestion() {

        return "В ответном сообщении" +
                " оставьте свой номер телефона" +
                " и email в формате: \n +7 123 456 78 90 example@email.com";
    }

    public void saveUser(Users user) {
        usersRepository.save(user);
    }

    public void sendMessageSuccessfulProbation(Long chatId) {
        String msg = "Уважаемый пользователь! Поздравляю с успешным прохождением испытательного срока!";
        SendMessage sendMessage = new SendMessage(chatId, msg);
        telegramBot.execute(sendMessage);
    }

    public void sendMessageFailureProbation(Long chatId) {
        String msg = "Уважаемый пользователь! К сожалению ваш испытательный срок завершился неуспешно!";
        SendMessage sendMessage = new SendMessage(chatId, msg);
        telegramBot.execute(sendMessage);
    }

    public void sendMessageWeekExtensionProbation(Long chatId) {
        String msg = "Уважаемый пользователь! Ваш испытательный срок продлен на 14 дней!";
        SendMessage sendMessage = new SendMessage(chatId, msg);
        telegramBot.execute(sendMessage);
    }

    public void sendMessageMonthExtensionProbation(Long chatId) {
        String msg = "Уважаемый пользователь! Ваш испытательный срок продлен на 30 дней!";
        SendMessage sendMessage = new SendMessage(chatId, msg);
        telegramBot.execute(sendMessage);
    }

    public void sendMessageBadReport(Long chatId) {
        String msg = "Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. " +
                "Пожалуйста, подойди ответственнее к этому занятию. " +
                "В противном случае волонтеры приюта будут обязаны самолично проверять условия содержания животного";
        SendMessage sendMessage = new SendMessage(chatId, msg);
        telegramBot.execute(sendMessage);
    }

    public Collection<Users> getAllUsers() {
        return usersRepository.findAll();
    }


}
