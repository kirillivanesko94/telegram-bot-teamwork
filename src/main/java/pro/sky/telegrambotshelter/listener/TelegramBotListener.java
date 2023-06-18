package pro.sky.telegrambotshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotshelter.entity.Report;
import pro.sky.telegrambotshelter.entity.User;
import pro.sky.telegrambotshelter.repository.UserRepository;
import pro.sky.telegrambotshelter.service.ReportService;
import pro.sky.telegrambotshelter.service.ShelterService;
import pro.sky.telegrambotshelter.service.ShelterVolunteerService;
import pro.sky.telegrambotshelter.shelter.ShelterType;
import pro.sky.telegrambotshelter.shelter.ShelterVolunteerType;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * This class is responsible for handling incoming updates
 */

@Service
public class TelegramBotListener implements UpdatesListener {
    /**
     * logger to get information about the received update
     */
    private final Logger logger = LoggerFactory.getLogger(TelegramBotListener.class);
    /**
     * constants for defining menu buttons in sent messages and designating CallbackQuery
     */
    private static final String DOG_SHELTER_BUTTON = "Приют для собак \uD83D\uDC36";
    private static final String CAT_SHELTER_BUTTON = "Приют для кошек \uD83D\uDC31";
    private static final String CALLBACK_CHOOSE_SHELTER_DOGS = "Choose_Shelter_Dogs";
    private static final String CALLBACK_CHOOSE_SHELTER_CATS = "Choose_Shelter_Cats";
    private static final String CALLBACK_SHOW_INFO_CATS = "SHOW_INFO_CATS";
    private static final String CALLBACK_SHOW_INFO_DOGS = "SHOW_INFO_DOGS";
    private static final String CALLBACK_SHOW_MENU_REPORT = "REPORT";
    private static final String CALLBACK_CHOOSE_SEND_REPORT = "SEND_REPORT";
    private static final String CALLBACK_CHOOSE_FORM_REPORT = "FORM_REPORT";
    private static final String CALLBACK_CALL_VOLUNTEER = "CALL_VOLUNTEER";
    private static final String CALL_VOLUNTEER_BUTTON = "Позвать волонтера";

    //учимся звать волонтера и смайлы
    private static final String CALLBACK_VOLUNTEER_BUTTON = "Нужна помощь волонтера \uD83D\uDE4F";
    private static final String CALLBACK_CHOOSE_SHELTER_VOLUNTEER = "CALL_ME_VOLUNTEER";
    private static final String CALLBACK_SHOW_INFO_VOLUNTEER = "INFO_VOLUNTEER";
    private static final String CALLBACK_SHOW_INSTRUCTION_DOGS = "SHOW_INSTRUCTION_DOGS";
    private static final String CALLBACK_SHOW_INSTRUCTION_CATS = "SHOW_INSTRUCTION_CATS";


    private final TelegramBot telegramBot;
    private final ShelterService shelterService;
    private final ReportService reportService;

    private final ShelterVolunteerService shelterVolunteerService;

    private final UserRepository userRepository;

    public TelegramBotListener(TelegramBot telegramBot, ShelterService shelterService,
                               ReportService reportService,
                               ShelterVolunteerService shelterVolunteerService, UserRepository userRepository) {
        this.telegramBot = telegramBot;
        this.shelterService = shelterService;
        this.reportService = reportService;
        this.shelterVolunteerService = shelterVolunteerService;
        this.userRepository = userRepository;
    }

    /**
     * the method of installing the telegram listener bot
     */
    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * update processing method
     *
     * @param updates available updates
     * @return confirmation of all received updates for continuous receipt of updates
     */

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            if (update.message() != null && "/start".equals(update.message().text())) {
                startMessage(update);
            } else if (update.callbackQuery() != null) {
                processCallbackQuery(update);
            } else if (update.message().photo() != null) {
                saveReportPhoto(update);
            } else if (update.message() != null && "Отчет".equalsIgnoreCase(update.message().text().substring(0, 5))) {
                saveReport(update);
            } else {
                failedMessage(update.message().chat().id());
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * Method for sending a welcome message
     * @param update received update
     */
    private void startMessage(Update update) {
        String name = update.message().chat().firstName();
        String msg = "Привет, " + name + "! Пожалуйста, выбери приют:";
        long id = update.message().chat().id();
        InlineKeyboardButton[] buttonsRow = {
                new InlineKeyboardButton(DOG_SHELTER_BUTTON).callbackData(CALLBACK_CHOOSE_SHELTER_DOGS),
                new InlineKeyboardButton(CAT_SHELTER_BUTTON).callbackData(CALLBACK_CHOOSE_SHELTER_CATS),
                new InlineKeyboardButton(CALLBACK_VOLUNTEER_BUTTON).callbackData(CALLBACK_CHOOSE_SHELTER_VOLUNTEER),
        };
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(buttonsRow);
        SendMessage sendMessage = new SendMessage(id, msg);
        sendMessage.replyMarkup(inlineKeyboard);
        telegramBot.execute(sendMessage);
    }

    /**
     * Method for processing incoming
     * @param update received update
     */

    private void processCallbackQuery(Update update) {
        Long chatId = update.callbackQuery().message().chat().id();

        if (CALLBACK_CHOOSE_SHELTER_DOGS.equalsIgnoreCase(update.callbackQuery().data())) {
            createButtonInfoMenuForDogShelter(chatId);
        } else if (CALLBACK_CHOOSE_SHELTER_CATS.equalsIgnoreCase(update.callbackQuery().data())) {
            createButtonInfoMenuForCatShelter(chatId);
        } else if (CALLBACK_SHOW_INFO_DOGS.equalsIgnoreCase(update.callbackQuery().data())) {
            sendShelterInfo(chatId, ShelterType.DOG);
        } else if (CALLBACK_SHOW_INFO_CATS.equalsIgnoreCase(update.callbackQuery().data())) {
            sendShelterInfo(chatId, ShelterType.CAT);
        } else if (CALLBACK_SHOW_MENU_REPORT.equalsIgnoreCase(update.callbackQuery().data())) {
            createButtonsReportMenu(chatId);
        } else if (CALLBACK_CHOOSE_SEND_REPORT.equalsIgnoreCase(update.callbackQuery().data())) {
            sendReportMessage(chatId);
        } else if (CALLBACK_CHOOSE_FORM_REPORT.equalsIgnoreCase(update.callbackQuery().data())) {
            sendReportForm(chatId);

//            делаем волонтера
        } else if (CALLBACK_CHOOSE_SHELTER_VOLUNTEER.equalsIgnoreCase(update.callbackQuery().data())) {
            createButtonInfoVolunteerMenu(chatId);
        } else if (CALLBACK_SHOW_INFO_VOLUNTEER.equalsIgnoreCase(update.callbackQuery().data())) {
            sendShelterVolunteerInfo(chatId, ShelterVolunteerType.VOLUNTEER);

        } else if (CALLBACK_SHOW_INSTRUCTION_DOGS.equalsIgnoreCase(update.callbackQuery().data())) {
            sendShelterInstruction(chatId, ShelterType.DOG);
        } else if (CALLBACK_SHOW_INSTRUCTION_CATS.equalsIgnoreCase(update.callbackQuery().data())) {
            sendShelterInstruction(chatId, ShelterType.CAT);
        } else {
            failedMessage(chatId);
        }
    }

    /**
     * Method to create menu Info
     * @param chatId - chat identifier
     * @param callbackShowInfoDogs - shelter
     * @param callbackShowInstructionDogs - shelter instructions
     */

    private void createButtonInfoMenu(Long chatId, String callbackShowInfoDogs, String callbackShowInstructionDogs) {
        String msg = "Пожалуйста, выберете следующее действие";
        InlineKeyboardButton[] buttonsRowForDogsShelter = {
                new InlineKeyboardButton("Информация о питомнике").callbackData(callbackShowInfoDogs),
                new InlineKeyboardButton("Информация о животных").callbackData(callbackShowInstructionDogs),
                new InlineKeyboardButton("Отчет").callbackData("report"),
        };
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(buttonsRowForDogsShelter);
        SendMessage sendMessage = new SendMessage(chatId, msg);
        sendMessage.replyMarkup(inlineKeyboard);
        telegramBot.execute(sendMessage);
    }

    /**
     * Method to create menu for cat shelter
     * @param chatId - chat identifier
     */
    private void createButtonInfoMenuForCatShelter(Long chatId) {
        createButtonInfoMenu(chatId, CALLBACK_SHOW_INFO_CATS, CALLBACK_SHOW_INSTRUCTION_CATS);
    }

    /**
     * Method to create menu for dog shelter
     * @param chatId - chat identifier
     */

    private void createButtonInfoMenuForDogShelter(Long chatId) {
        createButtonInfoMenu(chatId, CALLBACK_SHOW_INFO_DOGS, CALLBACK_SHOW_INSTRUCTION_DOGS);
    }

    /**
     * Method for helping a volunteer
     * @param chatId - chat identifier
     */
    private void createButtonInfoVolunteerMenu(Long chatId) {
        String msg = "Как мы можем вам помочь?";
        InlineKeyboardButton[] buttonsRowForVolunteerShelter = {
                new InlineKeyboardButton("тел гор линии 88005553535").callbackData(TelegramBotListener.CALLBACK_SHOW_INFO_VOLUNTEER),
                new InlineKeyboardButton("Напишите в чат волонтеру!").url("https://t.me/axel_27")
        };
        logger.warn("Пришел новый номер" + chatId);
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(buttonsRowForVolunteerShelter);
        SendMessage sendMessage = new SendMessage(chatId, msg);
        sendMessage.replyMarkup(inlineKeyboard);
        telegramBot.execute(sendMessage);
    }

    /**
     * Method to create menu Report
     *
     * @param chatId - chat identifier
     */
    private void createButtonsReportMenu(Long chatId) {
        String msg = "Пожалуйста, выберете следующее действие";
        InlineKeyboardButton[] buttonsRowForReportMenu = {
                new InlineKeyboardButton("Отправить отчет").callbackData(CALLBACK_CHOOSE_SEND_REPORT),
                new InlineKeyboardButton("Форма отчета").callbackData(CALLBACK_CHOOSE_FORM_REPORT),
        };
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(buttonsRowForReportMenu);
        SendMessage sendMessage = new SendMessage(chatId, msg);
        sendMessage.replyMarkup(inlineKeyboard);
        telegramBot.execute(sendMessage);
    }

    /**
     * method for sending information about the selected shelter
     * @param chatId - chat identifier
     * @param type   - Shelter type (Enum)
     */

    private void sendShelterInfo(Long chatId, ShelterType type) {
        SendMessage sendMessage = new SendMessage(chatId, shelterService.getInfo(type));
        telegramBot.execute(sendMessage);
    }
    /**
     * method for sending instruction
     * @param chatId - chat identifier
     * @param type   - Shelter type (Enum)
     */
    private void sendShelterInstruction(Long chatId, ShelterType type) {
        SendMessage sendMessage = new SendMessage(chatId, shelterService.getInstruction(type));
        telegramBot.execute(sendMessage);
    }

    /**
     * method send info about volunteer
     * @param chatId - chat identifier
     * @param type   - Shelter type (Enum)
     */
    private void sendShelterVolunteerInfo(Long chatId, ShelterVolunteerType type) {
        SendMessage sendMessage = new SendMessage(chatId, shelterVolunteerService.getInfoAboutQuestion(type));
        telegramBot.execute(sendMessage);

    }
    /**
     * Method to send info after clicking button Send Report
     *
     * @param chatId - chat identifier
     */
    private void sendReportMessage(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "В ответном сообщении напиши слово Отчет " +
                "и далее ежедневный отчет по форме. " +
                "Во втором сообщении пришли фото питомца, спасибо!");
        telegramBot.execute(sendMessage);
    }

    /**
     * Method to send report form after clicking button Report Form
     *
     * @param chatId - chat identifier
     */
    private void sendReportForm(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Вначале напиши слово Отчет " +
                "и далее необходимо описать:\n" +
                "- рацион питомца\n- общее самочувствие и привыкание к новому месту\n" +
                "- изменения в поведении: отказ от старых привычек, приобретение новых.\n" +
                "В следующем сообщении необходимо прислать фото питомца.");
        telegramBot.execute(sendMessage);
    }

    /**
     * Method to save report text
     *
     * @param update - update from chat-bot
     */
    private void saveReport(Update update) {
        Report report = new Report();
        report.setReportText(update.message().text().substring(5));
        User fakeUser = new User();
        fakeUser.setId(1L);
        report.setUser(fakeUser);
        reportService.reportTextSave(report);
        String msg = reportService.reportCheck();
        SendMessage sendMessage = new SendMessage(update.message().chat().id(), msg);
        telegramBot.execute(sendMessage);
    }

    /**
     * Method to save report photo
     *
     * @param update - update from chat-bot
     */
    private void saveReportPhoto(Update update) {
        PhotoSize[] photo = update.message().photo();

        GetFile request = new GetFile(photo[3].fileId());
        GetFileResponse getFileResponse = telegramBot.execute(request);
        File file = getFileResponse.file();
        reportService.reportPhotoSave(file);
        String msg = reportService.reportCheck();
        SendMessage sendMessage = new SendMessage(update.message().chat().id(), msg);
        telegramBot.execute(sendMessage);
    }

    /**
     * The method for sending the default message.
     * It is used in cases when the user has sent an unprocessed command
     * @param chatId - chat identifier
     */

    private void failedMessage(Long chatId) {
        String msg = "Извините, я не понимаю что делать";
        SendMessage sendMessage = new SendMessage(chatId, msg);
        telegramBot.execute(sendMessage);
    }

    /**
     * Method for entering the user's contacts into the database
     * @param chatId - chat identifier
     */
    private void volMessage(Long chatId) {
        String msg = "Тут надо в БД внести ваш номер(временная заглушка)";
        SendMessage sendMessage = new SendMessage(chatId, msg);
        telegramBot.execute(sendMessage);
    }
}


