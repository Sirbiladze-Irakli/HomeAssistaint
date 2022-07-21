package com.sirbiladze.homeassistaint.bot.handler;

import static com.sirbiladze.homeassistaint.utils.BotAnswerUtils.getRandomException;

import com.sirbiladze.homeassistaint.bot.cache.BotStateCache;
import com.sirbiladze.homeassistaint.bot.keyboards.ReplyKeyboardMaker;
import com.sirbiladze.homeassistaint.constants.BotMessageEnum;
import com.sirbiladze.homeassistaint.constants.BotStateEnum;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MessageHandler {

  ReplyKeyboardMaker replyKeyboardMaker;
  ToDoListHandler toDoListHandler;
  BotStateHandler botStateHandler;
  BotStateCache botStateCache;

  public BotApiMethod<?> answerMessage(Message message) {
    String chatId = message.getChatId().toString();
    String inputText = message.getText();
    String userName = message.getFrom().getUserName();
    BotStateEnum botState = botStateCache.getBotStateMap().get(chatId);
    EditMessageText editMessageText = new EditMessageText(chatId);
    SendMessage sendMessage = new SendMessage(chatId, "");

    if (inputText == null) {
      throw new IllegalArgumentException();
    }

//    if (botState != null && botState != BotStateEnum.NORMAL) {
//      return botStateHandler.processBotState(editMessageText, inputText, userName, botState);
//    }

//    switch (inputText) {
//      case ("/start") :
//        sendMessage = getStartMessage(chatId);
//        break;
//      case ("✏ \uD83D\uDDD3 Список дел \uD83D\uDDD3 \uD83D\uDCCC"):
//        sendMessage = toDoListHandler.getTodoList(chatId, userName);
//        break;
//      default:
//        sendMessage = new SendMessage(chatId, getRandomException());
//    }
    return sendMessage;
  }

  private SendMessage getStartMessage(String chatId) {
    SendMessage sendMessage = new SendMessage(chatId, BotMessageEnum.HELP_MESSAGE.getMessage());
    sendMessage.enableMarkdown(true);
    sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboard());
    return sendMessage;
  }

}
