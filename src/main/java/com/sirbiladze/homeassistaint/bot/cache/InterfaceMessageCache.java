package com.sirbiladze.homeassistaint.bot.cache;

import com.sirbiladze.homeassistaint.model.constants.BotStateEnum;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

/**
 * Кэш для хранения id сообщения с основным интерфейсом бота
 * создан для возможности редактирования интерфейса чтобы не захламлять чат
 * с клиентом и не отрисовывать его снова и снова.
 */

@Service
@Getter
@Setter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InterfaceMessageCache {

  Map<String, Integer> botStateMap = new HashMap<>();

  public void saveInterfaceMessage(String chatId, Integer messageId) {
    botStateMap.put(chatId, messageId);
  }

}
