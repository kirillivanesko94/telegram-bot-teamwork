package pro.sky.telegrambotshelter.service;

import com.pengrad.telegrambot.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotshelter.entity.Report;
import pro.sky.telegrambotshelter.entity.Users;
import pro.sky.telegrambotshelter.listener.TelegramBotListener;
import pro.sky.telegrambotshelter.repository.ReportRepository;
import pro.sky.telegrambotshelter.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VolunteerService {

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final TelegramBotListener listener;

    public VolunteerService(UserRepository userRepository, ReportRepository reportRepository, TelegramBotListener listener) {
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
        this.listener = listener;
    }
//возвращает идентификатор пользователя и последний отчет
    public Map<Long, Report> getAllLastReports() {
        return userRepository.findAll().stream()
                .filter(x -> x.getReports().size() != 0)
                .collect(Collectors.toMap(Users::getChatId, x -> x.getReports()
                        .stream()
                        .skip(x.getReports().size() - 1).findFirst().get()));
    }

    public String sendMessageAboutReport(Long chatId, boolean isGood){
        String message;
        if (isGood){
            message = "Ваш отчет принят";
        }else {
            message = "Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. " +
                    "Пожалуйста, подойди ответственнее к этому занятию." +
                    " В противном случае волонтеры приюта будут обязаны самолично проверять условия содержания животного";
        }
        listener.sendMessageFromVolunteer(chatId,message);
        return  "ответ отпрвлен";
    }
    public Collection<Report> getAllByChatId(Long chatId){
        return userRepository.findAll().stream()
                .filter(x ->x.getChatId() == chatId)
                .findFirst()
                .map(x -> x.getReports()).get();
    }

    public List<Long> getAllWhereCount(int count){
        return userRepository.findAll().stream()
                .filter(x -> x.getReports().size()>=count)
                .map(Users::getChatId).collect(Collectors.toList());
    }

    public String isPassedTheProbationPeriod(Long chatId, boolean isPassed){
        if(isPassed){
            listener.sendMessageFromVolunteer(chatId,"Поздравляем!Вы прошли испытательный период");
        }else{
            listener.sendMessageFromVolunteer(chatId,"В течение некоторого времени волонтер отправит вам сообщение опродлении испытательного преиода");
        }
        return  "ответ отпрвлен";
    }

    public String sendMessageAboutExtension(Long chatId, int days){
        listener.sendMessageFromVolunteer(chatId,"Ваш испытательный период был продлен на " + days + " дней");
        Users user = userRepository.findByChatId(chatId);
        List<Report> reports = reportRepository.findAllByUsers(user);
        if (!user.equals(null)) {
            userRepository.delete(user);
            reportRepository.deleteAll(reports);
        }
        return  "ответ отпрвлен";
    }
}