package pro.sky.telegrambotshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotshelter.service.ShelterService;
import pro.sky.telegrambotshelter.shelter.ShelterType;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotListener implements UpdatesListener {

    private static final String DOG_SHELTER_BUTTON = "Приют для собак";
    private static final String CAT_SHELTER_BUTTON = "Приют для кошек";
    private static final String CALLBACK_CHOOSE_SHELTER_DOGS = "Choose_Shelter_Dogs";
    private static final String CALLBACK_CHOOSE_SHELTER_CATS = "Choose_Shelter_Cats";
    private static final String CALLBACK_SHOW_INFO_CATS = "SHOW_INFO_CATS";
    private static final String CALLBACK_SHOW_INFO_DOGS = "SHOW_INFO_DOGS";

    private final TelegramBot telegramBot;
    private final ShelterService shelterService;
    public TelegramBotListener(TelegramBot telegramBot, ShelterService shelterService) {
        this.telegramBot = telegramBot;
        this.shelterService = shelterService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
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

    private void startMessage(Update update) {
        String name = update.message().chat().firstName();
        String msg = "Привет, " + name + "! Пожалуйста, выбери приют:";
        long id = update.message().chat().id();
        InlineKeyboardButton[] buttonsRow = {
                new InlineKeyboardButton(DOG_SHELTER_BUTTON).callbackData(CALLBACK_CHOOSE_SHELTER_DOGS),
                new InlineKeyboardButton(CAT_SHELTER_BUTTON).callbackData(CALLBACK_CHOOSE_SHELTER_CATS),
        };
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(buttonsRow);
        SendMessage sendMessage = new SendMessage(id, msg);
        sendMessage.replyMarkup(inlineKeyboard);
        telegramBot.execute(sendMessage);
    }

    private void processCallbackQuery(Update update) {
        Long chatId = update.callbackQuery().message().chat().id();

        if (CALLBACK_CHOOSE_SHELTER_DOGS.equalsIgnoreCase(update.callbackQuery().data())) {
            createButton(chatId, CALLBACK_SHOW_INFO_DOGS);
        } else if (CALLBACK_CHOOSE_SHELTER_CATS.equalsIgnoreCase(update.callbackQuery().data())) {
            createButton(chatId, CALLBACK_SHOW_INFO_CATS);
        } else if (CALLBACK_SHOW_INFO_DOGS.equalsIgnoreCase(update.callbackQuery().data())) {
            sendShelterInfo(chatId, ShelterType.DOG);
        } else if (CALLBACK_SHOW_INFO_CATS.equalsIgnoreCase(update.callbackQuery().data())) {
            sendShelterInfo(chatId, ShelterType.CAT);
        } else {
            failedMessage(chatId);
        }
    }

    private void createButton(Long chatId, String callbackShowInfoShelter) {
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


    private void sendShelterInfo(Long chatId, ShelterType type) {
        //TODO: Здесь нужно вызывать метод из сервиса, который в свою очередь берет информацию из БД.
        SendMessage sendMessage = new SendMessage(chatId, shelterService.getInfo(type));
        telegramBot.execute(sendMessage);
    }
    private void failedMessage(Long chatId){
        String msg = "Извините, я не понимаю что делать";
        SendMessage sendMessage = new SendMessage(chatId, msg);
        telegramBot.execute(sendMessage);
    }
}


