package com.sirbiladze.homeassistaint.bot.cache;

import com.sirbiladze.homeassistaint.model.constants.BotStateEnum;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BotStateCache {

  Map<String, BotStateEnum> botStateMap = new HashMap<>();

  public void saveBotState(String chatId, BotStateEnum botState) {
    botStateMap.put(chatId, botState);
  }

}
