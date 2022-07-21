package com.sirbiladze.homeassistaint.bot;

import com.sirbiladze.homeassistaint.bot.handler.MessageHandler;
import com.sirbiladze.homeassistaint.bot.handler.ToDoListHandler;
import com.sirbiladze.homeassistaint.utils.BotAnswerUtils;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AssistntBot extends TelegramLongPollingBot {

  MessageHandler messageHandler;
  ToDoListHandler toDoListHandler;

  public AssistntBot(MessageHandler messageHandler, ToDoListHandler toDoListHandler)
      throws TelegramApiException {
    this.messageHandler = messageHandler;
    this.toDoListHandler = toDoListHandler;
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
    CallbackQuery query = update.getCallbackQuery();
//    if (message != null) {
//
//      if (message.hasSticker() || message.hasAnimation()
//          || message.hasAudio() || message.hasPhoto() || message.hasVoice()) {
//        return new SendMessage(message.getChatId().toString(), BotAnswerUtils.getRandomException());
//      }
//      return messageHandler.answerMessage(message);
//    }
//    if (query != null) {
      return toDoListHandler.toDoListProcessing(message, query);
//    }
//    return null;
  }

}
