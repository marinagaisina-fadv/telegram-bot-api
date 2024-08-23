package com.marinagaisina.bot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "telegram-bot")
@Getter
@Setter
public class TelegramBotProperties {
    private String token;
    private String name;
}
