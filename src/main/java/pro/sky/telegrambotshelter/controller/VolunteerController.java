package pro.sky.telegrambotshelter.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.telegrambotshelter.service.ShelterVolunteerService;

@RestController
@RequestMapping("volunteer")
public class VolunteerController {
    ShelterVolunteerService shelterVolunteerService;

    public VolunteerController(ShelterVolunteerService shelterVolunteerService) {
        this.shelterVolunteerService = shelterVolunteerService;
    }
    @PostMapping("14-days")
    public void sendAMessageAboutAFourteenDayExtension(@RequestParam Long chatId){
        shelterVolunteerService.sendMessageWeekExtensionProbation(chatId);
    }
    @PostMapping("30-days")
    public void sendAMessageAboutAThirtyDayExtension(@RequestParam Long chatId){
        shelterVolunteerService.sendMessageMonthExtensionProbation(chatId);
    }
    @PostMapping("The trial period failed")
    public void sendMessageFailureProbation(@RequestParam Long chatId){
        shelterVolunteerService.sendMessageFailureProbation(chatId);
    }
    @PostMapping("Probation period passed")
    public void sendMessageSuccessfulProbation(@RequestParam Long chatId){
        shelterVolunteerService.sendMessageSuccessfulProbation(chatId);
    }
    @PostMapping("Bad-Report")
    public void sendMessageBadReport(@RequestParam Long chatId){
        shelterVolunteerService.sendMessageBadReport(chatId);
    }

}
