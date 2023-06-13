package pro.sky.telegrambotshelter.service;

import com.pengrad.telegrambot.model.File;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotshelter.entity.Report;
import pro.sky.telegrambotshelter.repository.ReportRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;

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
    private Report reportTmp = new Report();

    /**
     * Method-constructor for DI
     */
    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
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
            reportTmp.setFile(null);
            reportTmp.setReportText(null);
            reportTmp.setId(null);
            return "Отчет принят, спасибо!";
        } else if (reportTmp.getFile() != null && reportTmp.getReportText() == null) {
            return "В отчете не хватает текстовой части(";
        } else if (reportTmp.getReportText() != null && reportTmp.getFile() == null) {
            return "В отчете не хватает фото(";
        } else {
            return "Отчет пока пуст";
        }
    }
}
