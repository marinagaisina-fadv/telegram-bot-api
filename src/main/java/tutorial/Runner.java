package tutorial;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class Runner {
    private final Bot bot;

    public Runner(Bot bot) {
        this.bot = bot;
    }

    @PostConstruct
    private void start() {
        Utils.logger.info("bot.getBotUsername(): "+bot.getBotUsername());
        Utils.logger.info("bot.getBotToken(): "+bot.getBotToken());
        bot.onUpdateReceived(new Update());
    }
}
