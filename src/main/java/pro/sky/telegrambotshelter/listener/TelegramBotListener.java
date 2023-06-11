package pro.sky.telegrambotshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotshelter.entity.Animal;
import pro.sky.telegrambotshelter.entity.Shelter;
import pro.sky.telegrambotshelter.repository.AnimalRepository;
import pro.sky.telegrambotshelter.repository.ShelterRepository;
import pro.sky.telegrambotshelter.service.ShelterService;
import pro.sky.telegrambotshelter.shelter.ShelterType;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class TelegramBotListener implements UpdatesListener {

    private static final String DOG_SHELTER_BUTTON = "Приют для собак";
    private static final String CAT_SHELTER_BUTTON = "Приют для кошек";
    private static final String CALLBACK_CHOOSE_SHELTER_DOGS = "Choose_Shelter_Dogs";
    private static final String CALLBACK_CHOOSE_SHELTER_CATS = "Choose_Shelter_Cats";
    private static final String CALLBACK_SHOW_INFO_CATS = "SHOW_INFO_CATS";
    private static final String CALLBACK_SHOW_INFO_DOGS = "SHOW_INFO_DOGS";

    private final TelegramBot telegramBot;
    private final ShelterService shelterService;
    public TelegramBotListener(TelegramBot telegramBot, ShelterService shelterService, AnimalRepository animalRepository, ShelterRepository shelterRepository) {
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

    /**
     * метод обрабаотывает команнды
     * @param update
     */

    private void processCallbackQuery(Update update) {
        Long chatId = update.callbackQuery().message().chat().id();
        switch (update.callbackQuery().data()) {
            case (CALLBACK_CHOOSE_SHELTER_DOGS):
                createButton(chatId, CALLBACK_SHOW_INFO_DOGS);
                break;
            case (CALLBACK_CHOOSE_SHELTER_CATS):
                createButton(chatId, CALLBACK_SHOW_INFO_CATS);
                break;
            case (CALLBACK_SHOW_INFO_DOGS):
                sendShelterInfo(chatId, ShelterType.DOG,0);
                break;
            case (CALLBACK_SHOW_INFO_CATS):
                sendShelterInfo(chatId, ShelterType.CAT,0);
                break;
            case (SELECTED_SHELTER):

                break;
            case ("animal"):
                break;
            case ("report"):
                break;
            default:
                if (update.callbackQuery().data().split(" ").length == 3) {
                    String[] array = update.callbackQuery().data().split(" ");
                    if (array[0].equals(SELECTED_SHELTER)){
                        try {
                            returnInfoFromShelter(Integer.parseInt(array[1]));
                        }catch (NumberFormatException e) {
                            log.error("String format is not suitable");
                        }
                    }
                }
                failedMessage(chatId);
                break;
        }

    }

    /**
     *  метод обрабатывает команду выбора питомника
     * @param chatId
     * @param callbackShowInfoShelter
     */

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

    /**
     *метод выдает полную информацию о питомнике или питогмниках , если их несколько
     * @param chatId
     * @param type
     * @param page
     */
    private void sendShelterInfo(Long chatId, ShelterType type, int page) {
        List<Shelter> sheltersList = shelterService.getInfo(type);
        SendMessage sendMessage = new SendMessage(chatId, sheltersList.get(page).toString());
        if (sheltersList.size() != 0) {
            if (sheltersList.size() == 1) {
            }
            else if (page == 0) {
                InlineKeyboardButton[] buttonsRowForDogsShelter = {
                        new InlineKeyboardButton("Следующий питомник").callbackData("nextShelterOf" + " " + type.toString() + " " + (page + 1)),
                };
                InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(buttonsRowForDogsShelter);
                sendMessage.replyMarkup(inlineKeyboard);
            }
            else if (page == sheltersList.size() - 1) {
                InlineKeyboardButton[] buttonsRowForDogsShelter = {
                        new InlineKeyboardButton("Предыдущий питомник").callbackData("nextShelterOf" + " " + type.toString() + " " + (page - 1)),
                        new InlineKeyboardButton("Выбрать питомник").callbackData("SELECTED_SHELTER" + " " + sheltersList.get(page).getId())
                };
                InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(buttonsRowForDogsShelter);
                sendMessage.replyMarkup(inlineKeyboard);
            } else {
                InlineKeyboardButton[] buttonsRowForDogsShelter = {
                        new InlineKeyboardButton("Следующий питомник").callbackData("nextShelterOf" + " " + type.toString() + " " + (page + 1)),
                        new InlineKeyboardButton("Предыдущий питомник").callbackData("nextShelterOf" + " " + type.toString() + " " + (page - 1)),
                        new InlineKeyboardButton("Выбрать питомник").callbackData("SELECTED_SHELTER" + " " + sheltersList.get(page).getId())
                };
                InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(buttonsRowForDogsShelter);
                sendMessage.replyMarkup(inlineKeyboard);
            }
            telegramBot.execute(sendMessage);
        }
    }
    private void failedMessage(Long chatId){
        String msg = "Извините, я не понимаю что делать";
        SendMessage sendMessage = new SendMessage(chatId, msg);
        telegramBot.execute(sendMessage);
    }
    private void  returnInfoFromShelter(int shelterId){

    }
}


