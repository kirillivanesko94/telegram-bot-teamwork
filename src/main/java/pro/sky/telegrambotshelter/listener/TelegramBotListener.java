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
import pro.sky.telegrambotshelter.entity.Users;
import pro.sky.telegrambotshelter.repository.UserRepository;
import pro.sky.telegrambotshelter.service.ReportService;
import pro.sky.telegrambotshelter.service.ShelterService;
import pro.sky.telegrambotshelter.service.ShelterVolunteerService;
import pro.sky.telegrambotshelter.shelter.ShelterType;
import pro.sky.telegrambotshelter.shelter.ShelterVolunteerType;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    private static final String CALLBACK_VOLUNTEER_BUTTON = "Нужна помощь волонтера \uD83D\uDE4F";
    private static final String CALLBACK_CHOOSE_SHELTER_VOLUNTEER = "CALL_ME_VOLUNTEER";
    private static final String CALLBACK_SHOW_INFO_VOLUNTEER = "INFO_VOLUNTEER";
    private static final String CALLBACK_SHOW_INSTRUCTION_DOGS = "SHOW_INSTRUCTION_DOGS";
    private static final String CALLBACK_SHOW_INSTRUCTION_CATS = "SHOW_INSTRUCTION_CATS";
    private static final Pattern PATTERN = Pattern.compile("(^[+|8][0-9\\s]+)\\s(\\w*[@].+\\D$)");


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
            } else if (update.message() != null && checkMessagePattern(update.message().text())) {
                saveUser(update);
            } else {
                failedMessage(update.message().chat().id());
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * Method for sending a welcome message
     *
     * @param update received update
     */
    private void startMessage(Update update) {
        String name = update.message().chat().firstName();
        String msg = "Привет, " + name + "! Пожалуйста, выбери приют:";
        long id = update.message().chat().id();
        InlineKeyboardButton[] buttonsRow = {
                new InlineKeyboardButton(DOG_SHELTER_BUTTON).callbackData(CALLBACK_CHOOSE_SHELTER_DOGS)};
        InlineKeyboardButton[] buttonsRow2 = {
                new InlineKeyboardButton(CAT_SHELTER_BUTTON).callbackData(CALLBACK_CHOOSE_SHELTER_CATS)};
        InlineKeyboardButton[] buttonsRow3 = {
                new InlineKeyboardButton(CALLBACK_VOLUNTEER_BUTTON).callbackData(CALLBACK_CHOOSE_SHELTER_VOLUNTEER),
        };
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(buttonsRow, buttonsRow2, buttonsRow3);
        SendMessage sendMessage = new SendMessage(id, msg);
        sendMessage.replyMarkup(inlineKeyboard);
        telegramBot.execute(sendMessage);
    }

    /**
     * Method for processing incoming
     *
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
            sendShelterVolunteerInfo(chatId);
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
     *
     * @param chatId                      - chat identifier
     * @param callbackShowInfoDogs        - shelter
     * @param callbackShowInstructionDogs - shelter instructions
     */
    private void createButtonInfoMenu(Long chatId, String callbackShowInfoDogs, String callbackShowInstructionDogs) {
        String msg = "Пожалуйста, выберете следующее действие";
        InlineKeyboardButton[] buttonsRowForDogsShelter = {
                new InlineKeyboardButton("Информация о питомнике \uD83C\uDFD8 ").callbackData(callbackShowInfoDogs)};
        InlineKeyboardButton[] buttonsRowForDogsShelter2 = {
                new InlineKeyboardButton(" \uD83D\uDC15 Информация о животных \uD83D\uDC08").callbackData(callbackShowInstructionDogs)};
        InlineKeyboardButton[] buttonsRowForDogsShelter3 = {
                new InlineKeyboardButton("Отчет \uD83D\uDCDA ").callbackData("report"),
        };
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(buttonsRowForDogsShelter, buttonsRowForDogsShelter2, buttonsRowForDogsShelter3);
        SendMessage sendMessage = new SendMessage(chatId, msg);
        sendMessage.replyMarkup(inlineKeyboard);
        telegramBot.execute(sendMessage);
    }

    /**
     * Method to create menu for cat shelter
     *
     * @param chatId - chat identifier
     */
    private void createButtonInfoMenuForCatShelter(Long chatId) {
        createButtonInfoMenu(chatId, CALLBACK_SHOW_INFO_CATS, CALLBACK_SHOW_INSTRUCTION_CATS);
    }

    /**
     * Method to create for dog shelter
     *
     * @param chatId - chat identifier
     */

    private void createButtonInfoMenuForDogShelter(Long chatId) {
        createButtonInfoMenu(chatId, CALLBACK_SHOW_INFO_DOGS, CALLBACK_SHOW_INSTRUCTION_DOGS);
    }

    /**
     * Method for helping a volunteer
     *
     * @param chatId - chat identifier
     */

    private void createButtonInfoVolunteerMenu(Long chatId) {
        String msg = "Как мы можем вам помочь?";
        InlineKeyboardButton[] buttonsRowForVolunteerShelter = {
                new InlineKeyboardButton("Оставить свои контактные данные")
                        .callbackData(TelegramBotListener.CALLBACK_SHOW_INFO_VOLUNTEER)};
        InlineKeyboardButton[] buttonsRowForVolunteerShelter2 = {
                new InlineKeyboardButton("Напишите в чат волонтеру! ☎ ").url("https://t.me/axel_27")
        };
        logger.warn("Пришел новый номер" + chatId);
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(buttonsRowForVolunteerShelter, buttonsRowForVolunteerShelter2);
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
     *
     * @param chatId - chat identifier
     * @param type   - Shelter type (Enum)
     */

    private void sendShelterInfo(Long chatId, ShelterType type) {
        SendMessage sendMessage = new SendMessage(chatId, shelterService.getInfo(type));
        telegramBot.execute(sendMessage);
    }

    /**
     * method for sending instruction
     *
     * @param chatId - chat identifier
     * @param type   - Shelter type (Enum)
     */
    private void sendShelterInstruction(Long chatId, ShelterType type) {
        SendMessage sendMessage = new SendMessage(chatId, shelterService.getInstruction(type));
        telegramBot.execute(sendMessage);
    }

    /**
     * method send info about volunteer
     *
     * @param chatId - chat identifier
     */
    private void sendShelterVolunteerInfo(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, shelterVolunteerService.getInfoAboutQuestion(ShelterVolunteerType.VOLUNTEER));
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
        Users tmpUser = userRepository.findByChatId(update.message().chat().id());
        if (tmpUser == null) {
            String msg = "Отчет не принят, так как мы обнаружили, что текущий пользователь не зарегистрирован. " +
                    "Для корректной работы приложения - пожалуйста, зарегистрируйтесь в стартовом меню" +
                    " \"Нужна помощь волонтера\", спасибо)";
            SendMessage sendMessage = new SendMessage(update.message().chat().id(), msg);
            telegramBot.execute(sendMessage);
            return;
        }
        report.setUsers(tmpUser);
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

        GetFile request = new GetFile(photo[1].fileId());
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
     *
     * @param chatId - chat identifier
     */

    private void failedMessage(Long chatId) {
        String msg = "Извините, я не понимаю что делать";
        SendMessage sendMessage = new SendMessage(chatId, msg);
        telegramBot.execute(sendMessage);
    }

    private boolean checkMessagePattern(String text) {
        Matcher matcher = PATTERN.matcher(text);
        return matcher.matches();
    }

    private void saveUser(Update update) {
        Users user = new Users();
        String text = update.message().text();
        Matcher matcher = PATTERN.matcher(text);

        if (matcher.matches()) {
            user.setName(update.message().chat().firstName());
            user.setChatId(update.message().chat().id());
            user.setPhone(matcher.group(1));
            user.setEmail(matcher.group(2));

            shelterVolunteerService.saveUser(user);

            SendMessage sendMessage = new SendMessage(update.message().chat().id(), "Данные успешно сохранены!\n" +
                    "В ближайшее время с Вами свяжется волонтер");
            telegramBot.execute(sendMessage);
        } else {
            SendMessage sendMessage = new SendMessage(update.message().chat().id(), "Неверный формат номера телефона или email");
            telegramBot.execute(sendMessage);
        }




    }
}


