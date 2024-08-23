package com.marinagaisina.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.marinagaisina.bot.config.TelegramBotProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Component
@Data
@Slf4j
public class Bot extends TelegramLongPollingBot {
    @Autowired
    private TelegramBotProperties botProperties;

    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }

    @Override
    public String getBotToken() {
        return botProperties.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        //Utils.logger.info("update: "+upd ate.toString());
        var msg = update.getMessage();
        if (msg != null) {
            if (msg.isCommand() && msg.getText().equals("/start")) {
                executeStartCommand(msg);
            } else {
                //sendText(msg.getFrom().getId(),"Received message: \n" + msg.getText());

                // API CALL
                String answer;
                try {
                    answer = apiQuery(msg.getText());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                if (answer != null) {
                    // Print the response
                    //log.info("Answer: " + answer);
                    sendText(msg.getFrom().getId(), answer);
                    log.debug("Answer sent");
                } else {
                    log.error("Message is null.");
                }

            }
        } else {
            log.info("Message is null.");

        }
    }

    private String apiQuery(String question) throws JsonProcessingException {
        // Create the HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Prepare the request payload
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("question", question);

        // Convert request body to JSON
        ObjectMapper objectMapperReq = new ObjectMapper();
        String jsonRequestBody;
        try {
            jsonRequestBody = objectMapperReq.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("jsonRequestBody: "+jsonRequestBody);
        // Create the HTTP POST request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/ask"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody))
                .build();

        // Send the request and get the response
        HttpResponse<String> response;
        String answer;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //log.info("Response2: " + response.body());
            JSONObject orderJson = new JSONObject(response.body());
            answer = orderJson.getString("answer");

            log.info("Assistant's Message: " + answer);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return answer;
    }

    private void executeStartCommand(Message msg) {
        var user = msg.getFrom();
        Long userId = user.getId();
        //Utils.logger.info("Requested to start.");
        String lastName = user.getLastName() == null ? "" : user.getLastName();
        String introduction = "Hi, "+user.getFirstName()+" "+lastName+"! \nWelcome to Hackatron Bot!";
        sendText(userId, introduction);
        //Utils.logger.info(user.getFirstName() + " wrote " + msg.getText());
    }

    @Override
    public void onRegister() {
        System.out.println("The bot has been registered!");
    }

    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }

}

// goal to automate everything

