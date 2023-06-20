package pro.sky.telegrambotshelter.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @ApiResponse(
            responseCode = "200",
            description = "the method sends a message to the user about the extension of the trial period for 14 days"
    )
    @PostMapping("14-days")
    public void sendAMessageAboutAFourteenDayExtension(@RequestParam Long chatId){
        shelterVolunteerService.sendMessageWeekExtensionProbation(chatId);
    }

    @ApiResponse(
            responseCode = "200",
            description = "the method sends a message to the user about the extension of the trial period for 30 days"
    )
    @PostMapping("30-days")
    public void sendAMessageAboutAThirtyDayExtension(@RequestParam Long chatId){
        shelterVolunteerService.sendMessageMonthExtensionProbation(chatId);
    }

    @ApiResponse(
            responseCode = "200",
            description = "the method sends a message to the user that he has not passed the probation period"
    )
    @PostMapping("The trial period failed")
    public void sendMessageFailureProbation(@RequestParam Long chatId){
        shelterVolunteerService.sendMessageFailureProbation(chatId);
    }

    @ApiResponse(
            responseCode = "200",
            description = "the method sends a message to the user that he has passed the probation period"
    )
    @PostMapping("Probation period passed")
    public void sendMessageSuccessfulProbation(@RequestParam Long chatId){
        shelterVolunteerService.sendMessageSuccessfulProbation(chatId);
    }

    @ApiResponse(
            responseCode = "200",
            description = "the method sends a message to the user that the report has not been accepted"
    )
    @PostMapping("Bad-Report")
    public void sendMessageBadReport(@RequestParam Long chatId){
        shelterVolunteerService.sendMessageBadReport(chatId);
    }

}
