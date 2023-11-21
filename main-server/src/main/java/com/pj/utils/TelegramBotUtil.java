package com.pj.utils;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBotUtil extends TelegramLongPollingBot {

//    private static final String BOT_TOKEN = "1356952146:AAGi9C4XbTAHPMeaQSMqtjm5rprFMjA_P-Q";

    private String botToken;

    private static final String CHAT_ID = "-4027547464";

    public TelegramBotUtil(String botToken) {
        this.botToken = botToken;
    }

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
        return botToken; // Return your bot's token
    }

    public void sendMessageToGroup(String chatId, String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageText);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TelegramBotUtil bot = new TelegramBotUtil("botToken");
        // Replace YOUR_CHAT_ID with your group's Chat ID
        String chatId = "-4027547464";
        String message = "test message";

        bot.sendMessageToGroup(chatId, message);
    }
}

