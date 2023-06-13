package pro.sky.telegrambotshelter.service;

import com.pengrad.telegrambot.model.File;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotshelter.entity.Report;
import pro.sky.telegrambotshelter.repository.ReportRepository;

/**
 * Service class for report
 * @autor Egor
 */
@Service
public class ReportService {

    /**
     * Method-constructor for DI
     */
    private final ReportRepository reportRepository;
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }


    public void reportSave(Report report) {
        reportRepository.save(report);
    }
    public void photoSave(File file) {
        Report report = reportRepository.findReportByLastId();
        report.setFile(file);
//        reportRepository.updateReportById(report, report.getId());
    }
}
