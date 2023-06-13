package pro.sky.telegrambotshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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
    private Logger logger = LoggerFactory.getLogger(TelegramBotListener.class);
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

    private final TelegramBot telegramBot;
    private final ShelterService shelterService;
    private final ShelterVolunteerService shelterVolunteerService;

    public TelegramBotListener(TelegramBot telegramBot, ShelterService shelterService, ShelterVolunteerService shelterVolunteerService) {
        this.telegramBot = telegramBot;
        this.shelterService = shelterService;
        this.shelterVolunteerService = shelterVolunteerService;
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
            } else {
                // Потенциально NPE потому что message может быть null
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
            createButtonInfoMenu(chatId, CALLBACK_SHOW_INFO_DOGS);
        } else if (CALLBACK_CHOOSE_SHELTER_CATS.equalsIgnoreCase(update.callbackQuery().data())) {
            createButtonInfoMenu(chatId, CALLBACK_SHOW_INFO_CATS);
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
            createButtonInfoVolunteerMenu(chatId, CALLBACK_SHOW_INFO_VOLUNTEER);
        } else if (CALLBACK_SHOW_INFO_VOLUNTEER.equalsIgnoreCase(update.callbackQuery().data())) {
            sendShelterVolunteerInfo(chatId, ShelterVolunteerType.VOLUNTEER);

    } else {
            failedMessage(chatId);
        }
    }

    /**
     * Method to create menu Info
     * @param chatId - chat identifier
     * @param callbackShowInfoShelter - the assigned value of the button, for processing incoming CallbackQuery
     */

    private void createButtonInfoMenu(Long chatId, String callbackShowInfoShelter) {
        String msg = "Пожалуйста, выберете следующее действие";
        InlineKeyboardButton[] buttonsRowForDogsShelter = {
                new InlineKeyboardButton("Информация о питомнике").callbackData(callbackShowInfoShelter),
                new InlineKeyboardButton("Информация о животных").callbackData("animal"),
                new InlineKeyboardButton("Отчет").callbackData("report"),
        };
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(buttonsRowForDogsShelter);
        SendMessage sendMessage = new SendMessage(chatId, msg);
        sendMessage.replyMarkup(inlineKeyboard);
        telegramBot.execute(sendMessage);
    }
    // для волонтера
    private void createButtonInfoVolunteerMenu(Long chatId, String callbackShowInfoShelter) {
        String msg = "Как мы можем вам помочь?";
        InlineKeyboardButton[] buttonsRowForVolunteerShelter = {
                new InlineKeyboardButton("тел гор линии 88005553535").callbackData(callbackShowInfoShelter),
                new InlineKeyboardButton("Напишите в чат ваш вопрос, волонтер поможет!").switchInlineQuery("Напишите нашему волонтеру, он поможет https://t.me/axel_27"),

        };
//        пока временно появляется варн в консоли, надо из "сюда пиши позвоним -закинуть в БД номер"
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
     * @param type - Shelter type (Enum)
     */

    private void sendShelterInfo(Long chatId, ShelterType type) {
        //TODO: Здесь нужно вызывать метод из сервиса, который в свою очередь берет информацию из БД.
        SendMessage sendMessage = new SendMessage(chatId, shelterService.getInfo(type));
        InlineKeyboardButton[] buttonsRowForDogsShelter = {
                new InlineKeyboardButton(CALL_VOLUNTEER_BUTTON).callbackData(CALLBACK_CALL_VOLUNTEER),
        };
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(buttonsRowForDogsShelter);
        sendMessage.replyMarkup(inlineKeyboard);
        telegramBot.execute(sendMessage);
    }

    //    для волонтера
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
        SendMessage sendMessage = new SendMessage(chatId, "В ответном сообщении напиши ежедневный отчет по форме. " +
                "Не забудь прикрепить к сообщению фото питомца, спасибо!");
        telegramBot.execute(sendMessage);
    }

    /**
     * Метод для отправки формы отчета после нажатия кнопки "Форма отчета"
     * Method to send report form after clicking button Report Form
     *
     * @param chatId - chat identifier
     */
    private void sendReportForm(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Необходимо описать:\n" +
                "- рацион питомца\n- общее самочувствие и привыкание к новому месту\n" +
                "- изменения в поведении: отказ от старых привычек, приобретение новых.\n" +
                "Также необходимо прикрепить к сообщению фото питомца.");
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
    //для волонтера
    private void volMessage(Long chatId) {
        String msg = "Тут надо в БД внести ваш номер(временная заглушка)";
        SendMessage sendMessage = new SendMessage(chatId, msg);
        telegramBot.execute(sendMessage);
    }
}


