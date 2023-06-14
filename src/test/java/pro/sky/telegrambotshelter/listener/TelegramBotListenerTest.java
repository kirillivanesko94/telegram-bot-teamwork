package pro.sky.telegrambotshelter.listener;

import com.google.gson.Gson;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.impl.TelegramBotClient;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pro.sky.telegrambotshelter.service.ShelterService;
import pro.sky.telegrambotshelter.service.ShelterVolunteerService;

/**
 * This class is responsible for testing TelegramBotListener.class methods
 */
@ContextConfiguration(classes = {TelegramBotListener.class})
@ExtendWith(SpringExtension.class)
class TelegramBotListenerTest {
    @MockBean
    private TelegramBot telegramBot;
    @MockBean
    private ShelterService shelterService;
    @MockBean
    private ShelterVolunteerService shelterVolunteerService;

    private final TelegramBotListener telegramBotListener;

    @Autowired
    public TelegramBotListenerTest(TelegramBotListener telegramBotListener) {
        this.telegramBotListener = telegramBotListener;
    }

    @Test
    void processWithStartMessage() {
    }

    @Test
    void createButtonsReportMenuTest() {
        Long chatId = 1L;
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        String url = "https://sky.pro/";
        TelegramBotClient api = new TelegramBotClient(client, gson, url);

//        when(telegramBot.execute(any())).thenReturn(api.send(any(BaseRequest.class)));

    }

    @Test
    void init() {
    }

}