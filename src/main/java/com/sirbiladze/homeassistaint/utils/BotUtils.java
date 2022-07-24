package com.sirbiladze.homeassistaint.utils;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@UtilityClass
public class BotUtils {

  public static DeleteMessage deleteUsersMessage(Message message) {
    return new DeleteMessage(message.getChatId().toString(), message.getMessageId());
  }

}
