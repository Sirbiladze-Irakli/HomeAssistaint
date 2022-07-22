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
  EDIT_TASK_TITLE("Редактировать название задачи"),
  ADD_DESCRIPTION("Добавить описание"),
  EDIT_DESCRIPTION("Редактировать описание"),
  CHANGE_STATUS("Изменить статус"),
  DELETE_OR_NOT("Удалить задачу"),
  DELETE_TASK("Удалить"),
  BACK_TO_TASK_DETAIL("Вернуться к задаче"),
  BACK_TO_TO_DO_LIST("Вернуться к списку дел"),
  BACK_TO_MAIN_MENU("Вернуться в главное меню");

  String buttonText;

}
