package pro.sky.telegrambotshelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotshelter.repository.UserRepository;

import pro.sky.telegrambotshelter.shelter.ShelterVolunteerType;

@Service

public class ShelterVolunteerService {
    private final UserRepository userRepository;
    private final TelegramBot telegramBot;
    public ShelterVolunteerService(UserRepository userRepository, TelegramBot telegramBot) {
        this.userRepository = userRepository;
        this.telegramBot = telegramBot;
    }
    public String getInfoAboutQuestion(ShelterVolunteerType type) {

        return "ВПожалуйста укажите ваш контактный номер телефона, в ближайшее время с вами свяжется волонтер, чтоб решить ваш вопрос. (заглушка)";
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






}
