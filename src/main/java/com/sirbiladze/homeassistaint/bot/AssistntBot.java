package com.sirbiladze.homeassistaint.bot;

import com.sirbiladze.homeassistaint.bot.handler.BotHandler;
import com.sirbiladze.homeassistaint.bot.handler.MainMenuHandler;
import com.sirbiladze.homeassistaint.bot.handler.CallbackQueryHandler;
import com.sirbiladze.homeassistaint.bot.handler.MessageHandler;
import com.sirbiladze.homeassistaint.constants.BotCommandsEnum;
import com.sirbiladze.homeassistaint.constants.CallbackDataEnum;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AssistntBot extends TelegramLongPollingBot {

  CallbackQueryHandler callbackQueryHandler;
  MainMenuHandler mainMenuHandler;
  MessageHandler messageHandler;

  public AssistntBot(CallbackQueryHandler callbackQueryHandler, MainMenuHandler mainMenuHandler,
      MessageHandler messageHandler) throws TelegramApiException {
    this.callbackQueryHandler = callbackQueryHandler;
    this.mainMenuHandler = mainMenuHandler;
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
    List<BotApiMethod<?>> methods = handleUpdate(update);
      methods.forEach(botApiMethod -> {
        try {
          execute(botApiMethod);
        } catch (TelegramApiException e) {
          e.printStackTrace();
        }
      });
  }

  private List<BotApiMethod<?>> handleUpdate(Update update) {
    Message message = update.getMessage();
    CallbackQuery query = update.getCallbackQuery();
    List<BotApiMethod<?>> methods;

    if (message != null && message.getText().equals(BotCommandsEnum.START.getCommand())) {
      methods = mainMenuHandler.getMainMenuFromStart(message);
    } else if (query != null &&
        query.getData().equals(CallbackDataEnum.BACK_TO_MAIN_MENU.getCallbackData())) {
      methods = mainMenuHandler.backToMainMenu(query);
    } else if (message != null) {
      methods = messageHandler.processMessage(message);
    } else {
      methods = callbackQueryHandler.processQuery(query);
    }
    return methods;
  }

}
