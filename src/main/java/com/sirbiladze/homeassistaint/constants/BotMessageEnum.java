package com.sirbiladze.homeassistaint.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum BotMessageEnum {

  HELP_MESSAGE("Привет! ✌ \n"
      + "Меня зовут Дарий и я создан для того, чтобы помогать в повседневных делах. "
      + "Постараюсь тебя не разочаровать!\uD83D\uDE09"),

  EXCEPTION_ILLEGAL_MESSAGE1("Ничего непонятно, но очень интересно!"),
  EXCEPTION_ILLEGAL_MESSAGE2("Меня к такому не готовили..."),
  EXCEPTION_ILLEGAL_MESSAGE3("Задай мне этот вопрос когда меня обновят, ладно?"),
  EXCEPTION_ILLEGAL_MESSAGE4("Мой создатель не удосужился научить меня всем людским примудростям.");

  String message;

}
