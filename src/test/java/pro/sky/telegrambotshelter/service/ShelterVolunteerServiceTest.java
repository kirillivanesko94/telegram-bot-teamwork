package pro.sky.telegrambotshelter.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pro.sky.telegrambotshelter.repository.UserRepository;
import pro.sky.telegrambotshelter.shelter.ShelterVolunteerType;

import static pro.sky.telegrambotshelter.shelter.ShelterVolunteerType.VOLUNTEER;

/**
 * This class is responsible for testing ShelterVolunteerService.class methods
 */
@ContextConfiguration(classes = {ShelterVolunteerService.class})
@ExtendWith(SpringExtension.class)
class ShelterVolunteerServiceTest {

    @MockBean
    private UserRepository userRepository;
    private final ShelterVolunteerService shelterVolunteerService;

    @Autowired
    public ShelterVolunteerServiceTest(ShelterVolunteerService shelterVolunteerService) {
        this.shelterVolunteerService = shelterVolunteerService;
    }

    /**
     * Method for testing shelterVolunteerService.getInfoAboutQuestion
     */
    @Test
    void getInfoAboutQuestionTest() {
        String expectedResult = "ВПожалуйста укажите ваш контактный номер телефона, " +
                "в ближайшее время с вами свяжется волонтер, чтоб решить ваш вопрос. (заглушка)";
        ShelterVolunteerType type = VOLUNTEER;

        Assertions.assertEquals(expectedResult, shelterVolunteerService.getInfoAboutQuestion(type));
    }
}