package com.sirbiladze.homeassistaint.bot.handler;

import static com.sirbiladze.homeassistaint.utils.BotAnswerUtils.getRandomException;

import com.sirbiladze.homeassistaint.bot.keyboards.ReplyKeyboardMaker;
import com.sirbiladze.homeassistaint.constants.BotMessageEnum;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MessageHandler {

  ReplyKeyboardMaker replyKeyboardMaker;

  public SendMessage answerMessage(Message message) {
    String chatId = message.getChatId().toString();
    String inputText = message.getText();
    String userName = message.getFrom().getUserName();
    SendMessage sendMessage;

    if (inputText == null) {
      throw new IllegalArgumentException();
    }

    switch (inputText) {
      case ("/start") :
        sendMessage = getStartMessage(chatId);
        break;
      case ("✏ \uD83D\uDDD3 Список дел \uD83D\uDDD3 \uD83D\uDCCC"):
        sendMessage = getTodoList(chatId, userName);
        break;
      default:
        sendMessage = new SendMessage(chatId, getRandomException());
    }
    return sendMessage;
  }

  private SendMessage getStartMessage(String chatId) {
    SendMessage sendMessage = new SendMessage(chatId, BotMessageEnum.HELP_MESSAGE.getMessage());
    sendMessage.enableMarkdown(true);
    sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboard());
    return sendMessage;
  }

  private SendMessage getTodoList(String chatId, String userName) {

    return null;
  }

}
