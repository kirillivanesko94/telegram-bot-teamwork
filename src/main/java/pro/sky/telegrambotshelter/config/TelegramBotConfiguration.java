package pro.sky.telegrambotshelter.config;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class is responsible for the configuration of the telegram bot
 */
@Configuration
public class TelegramBotConfiguration {
    /**
     * token - API received during registration
     */
    @Value("${telegram.bot.token}")
    private String token;

    /**
     * Method for creating telegram bot
     */
    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        return bot;
    }
}
