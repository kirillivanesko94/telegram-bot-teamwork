package pro.sky.telegrambotshelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotshelter.entity.Report;
import pro.sky.telegrambotshelter.repository.ReportRepository;
import pro.sky.telegrambotshelter.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for report
 *
 * @autor Egor
 */
@Service
public class ReportService {
    /**
     * Field to temporary store fields of report
     */
    private Logger logger = LoggerFactory.getLogger(ReportService.class);
    private Report reportTmp = new Report();
    private Map<Long, LocalDateTime> timeOfLastReport = new HashMap<>();

    /**
     * Method-constructor for DI
     */
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final TelegramBot telegramBot;

    public ReportService(ReportRepository reportRepository, UserRepository userRepository, TelegramBot telegramBot) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.telegramBot = telegramBot;
    }

    /**
     * Method to save report text
     *
     * @param report - report from chat-bot
     */
    public void reportTextSave(Report report) {
        reportTmp.setReportText(report.getReportText());
        reportTmp.setUsers(report.getUsers());
    }

    /**
     * Method to save report photo
     *
     * @param file - photo from chat-bot
     */
    public void reportPhotoSave(File file) {
        reportTmp.setFile(file);
    }

    /**
     * Method to check temporary report
     *
     */
    public String reportCheck() {
        if (reportTmp.getFile() != null && reportTmp.getReportText() != null) {
            reportRepository.save(reportTmp);
            timeOfLastReport.put(reportTmp.getUsers().getId(), LocalDateTime.now());
            reportTmp.setFile(null);
            reportTmp.setReportText(null);
            reportTmp.setId(null);
            return "Отчет принят, спасибо!";
        } else if (reportTmp.getFile() != null) {
            return "В отчете не хватает текстовой части(";
        } else if (reportTmp.getReportText() != null) {
            return "В отчете не хватает фото(";
        } else {
            return "Отчет пока пуст";
        }
    }

    /**
     * Method to check reports was sent in-time
     *
     */
    @Scheduled(cron = "0 0/1 * * * *")
    private void reportReminder() {
        logger.info("Method reportReminder was invoked");
        System.out.println(timeOfLastReport.toString());
        String reminderMessage = "Напоминаю, что сегодня ты еще не присылал отчет(";
        String volunteerMessage = "К сожалению, ты не присылал отчет более двух дней( Я вынужден сообщить об этом волонтеру(";
        Collection<Long> idUsersWithoutReports = timeOfLastReport.entrySet().stream()
                .filter(x -> ((LocalDateTime.now().getDayOfMonth() - x.getValue().getDayOfMonth()) != 0))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        idUsersWithoutReports
                .forEach(x -> {
                    telegramBot.execute(
                            new SendMessage(
                                    userRepository.findById(x).get().getChatId(),
                                    reminderMessage));
                });

        Collection<Long> idUsersWithoutReportsOverTwoDays = timeOfLastReport.entrySet().stream()
                .filter(x -> ((LocalDateTime.now().getDayOfMonth() - x.getValue().getDayOfMonth()) > 1))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        idUsersWithoutReportsOverTwoDays
                .forEach(x -> {
                    telegramBot.execute(
                            new SendMessage(
                                    userRepository.findById(x).get().getChatId(),
                                    volunteerMessage));
                });

    }
}
