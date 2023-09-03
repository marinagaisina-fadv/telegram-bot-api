package tutorial;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@EqualsAndHashCode(callSuper = true)
@Component
@Data
public class Bot extends TelegramLongPollingBot {
    @Value("${BOT_TOKEN:incorrect token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return "@javaMG_bot";
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Utils.logger.info("update: "+update);
    }

}

