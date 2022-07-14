package com.sirbiladze.homeassistaint.utils;

import com.sirbiladze.homeassistaint.constants.BotMessageEnum;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@SuppressWarnings("java:S2140")
public class BotAnswerUtils {

  private BotAnswerUtils() {
    throw new IllegalStateException();
  }

  static List<String> exceptions = List.of(BotMessageEnum.EXCEPTION_ILLEGAL_MESSAGE1.getMessage(),
      BotMessageEnum.EXCEPTION_ILLEGAL_MESSAGE2.getMessage(),
      BotMessageEnum.EXCEPTION_ILLEGAL_MESSAGE3.getMessage(),
      BotMessageEnum.EXCEPTION_ILLEGAL_MESSAGE4.getMessage());

  public static String getRandomException() {
    return exceptions.get((int) (Math.random() * exceptions.size()));
  }

}
