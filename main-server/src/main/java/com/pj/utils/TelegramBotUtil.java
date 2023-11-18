package com.pj.utils;

import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBotUtil extends TelegramLongPollingBot {

    @Value("${telegram.robot.token}")
    private String token;

    @Value("${telegram.chat.groupId}")
    private String groupId;

    @Override
    public void onUpdateReceived(org.telegram.telegrambots.meta.api.objects.Update update) {
        // Implement your logic here if needed
    }

    @Override
    public String getBotUsername() {
        return "YourBotUsername"; // Return your bot's username
    }

    @Override
    public String getBotToken() {
        return token; // Return your bot's token
    }

    public void sendMessageToGroup(String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(groupId);
        message.setText(messageText);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TelegramBotUtil bot = new TelegramBotUtil();
        // Replace YOUR_CHAT_ID with your group's Chat ID
        String chatId = "-4027547464";
        String message = "test message";

        bot.sendMessageToGroup(message);
    }
}

