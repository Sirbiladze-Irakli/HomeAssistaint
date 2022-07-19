package com.sirbiladze.homeassistaint.constants;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum BotButtonTextEnum {

  TO_DO_LIST("✏ \uD83D\uDDD3 Список дел \uD83D\uDDD3 \uD83D\uDCCC"),
  ADD_NEW_TASK("Добавить новую задачу \uD83D\uDDD3"),
  EMPTY_TASK("➖"),
  CHANGE_STATUS("Изменить статус"),
  DELETE("Удалить"),
  BACK("Назад");


  String buttonText;

}
