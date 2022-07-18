package com.sirbiladze.homeassistaint.constants;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum BotMessageEnum {

  HELP_MESSAGE("Привет! ✌ \n"
      + "Меня зовут Дарий и я создан для того, чтобы помогать в повседневных делах. "
      + "Постараюсь тебя не разочаровать!\uD83D\uDE09"),

  TO_DO_LIST("Список дел"),

  HOW_TO_ADD_NEW_TASK("Для создания новой задачи напишите текст по примеру ниже ⬇️\n\n"
      + "Задача\n"
      + "- Название\n"
      + "- Описание"),

  TASK_DETAIL("%s\n"
      + "Описание: %s\n"
      + "Статус по задаче: \"%s\"\n"),

  STATUS_UPDATE_MESSAGE("Выберите новый статус задачи"),

  DELETE_TASK("Задача \"%s\" была удалена"),

  EXCEPTION_ILLEGAL_MESSAGE1("Ничего непонятно, но очень интересно!"),
  EXCEPTION_ILLEGAL_MESSAGE2("Меня к такому не готовили..."),
  EXCEPTION_ILLEGAL_MESSAGE3("Задай мне этот вопрос когда меня обновят, ладно?"),
  EXCEPTION_ILLEGAL_MESSAGE4("Мой создатель не удосужился научить меня всем людским примудростям.");

  String message;

}
