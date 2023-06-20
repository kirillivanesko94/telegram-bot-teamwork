package pro.sky.telegrambotshelter.controller;

import com.pengrad.telegrambot.TelegramBot;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pro.sky.telegrambotshelter.entity.Users;
import pro.sky.telegrambotshelter.repository.UsersRepository;
import pro.sky.telegrambotshelter.service.ShelterVolunteerService;

import javax.annotation.PostConstruct;
import javax.validation.constraints.AssertFalse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VolunteerController.class)
class VolunteerControllerTest {

    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private TelegramBot telegramBot;

    @SpyBean
    private ShelterVolunteerService shelterVolunteerService;
    private final VolunteerController volunteerController;
    private final MockMvc mockMvc;

    @Autowired
    VolunteerControllerTest(VolunteerController volunteerController, MockMvc mockMvc) {
        this.volunteerController = volunteerController;
        this.mockMvc = mockMvc;
    }

    private static final JSONObject chatIdObject = new JSONObject();
    private static final Users CORRECT_USER = new Users();
    private static final Collection<Users> CORRECT_USER_COLLECTION = new ArrayList<>();


    @PostConstruct
    public void initData() throws JSONException {
        Long id = 1L;
        String name = "A";
        String email = "1";
        String phone = "1";
        Long chatId = 1L;
        CORRECT_USER.setId(id);
        CORRECT_USER.setName(name);
        CORRECT_USER.setEmail(email);
        CORRECT_USER.setPhone(phone);
        CORRECT_USER.setChatId(chatId);
        CORRECT_USER_COLLECTION.add(CORRECT_USER);

        chatIdObject.put("chatId", CORRECT_USER.getChatId());
    }

    @Test
    void sendAMessageAboutAFourteenDayExtension() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/volunteer/14-days")
                        .param("chatId", CORRECT_USER.getChatId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void sendAMessageAboutAThirtyDayExtension() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/volunteer/30-days")
                        .param("chatId", CORRECT_USER.getChatId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void sendMessageFailureProbation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/volunteer/The trial period failed")
                        .param("chatId", CORRECT_USER.getChatId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void sendMessageSuccessfulProbation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/volunteer/Probation period passed")
                        .param("chatId", CORRECT_USER.getChatId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void sendMessageBadReport() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/volunteer/Bad-Report")
                        .param("chatId", CORRECT_USER.getChatId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllUsers() throws Exception {
        when(usersRepository.findAll()).thenReturn((List<Users>) CORRECT_USER_COLLECTION);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer/all-users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(CORRECT_USER.getId()))
                .andExpect(jsonPath("[0].name").value(CORRECT_USER.getName()))
                .andExpect(jsonPath("[0].email").value(CORRECT_USER.getEmail()))
                .andExpect(jsonPath("[0].phone").value(CORRECT_USER.getPhone()))
                .andExpect(jsonPath("[0].chatId").value(CORRECT_USER.getChatId()));
    }
}