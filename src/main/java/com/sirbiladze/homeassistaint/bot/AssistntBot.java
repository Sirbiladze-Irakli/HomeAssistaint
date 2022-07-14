package com.sirbiladze.homeassistaint.bot;

import com.sirbiladze.homeassistaint.bot.handler.MessageHandler;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssistntBot extends TelegramLongPollingBot {

  MessageHandler messageHandler;

  public AssistntBot(MessageHandler messageHandler) throws TelegramApiException {
    this.messageHandler = messageHandler;
    TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
    telegramBotsApi.registerBot(this);
  }

  @Override
  public String getBotUsername() {
    return System.getenv("TELEGRAM_BOT_NAME");
  }

  @Override
  public String getBotToken() {
    return System.getenv("TELEGRAM_BOT_TOKEN");
  }

  @Override
  public void onUpdateReceived(Update update) {
    try {
      execute(handleUpdate(update));
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  private BotApiMethod<?> handleUpdate(Update update) {
    Message message = update.getMessage();
    if (message != null) {
      return messageHandler.answerMessage(update.getMessage());
    }
    return null;
  }

}
