package com.marinagaisina.bot;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@Slf4j
public class Runner {
    private final Bot bot;

    public Runner(Bot bot) {
        this.bot = bot;
    }

    @PostConstruct
    private void start() {
        //bot.onRegister();
        log.info("bot.getBotUsername(): {}", bot.getBotUsername());
        log.info("bot.getBotToken(): {}", bot.getBotToken());
        bot.onUpdateReceived(new Update());

        TelegramBotsApi botsApi;
        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
