package com.sirbiladze.homeassistaint.model.constants;

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
      + "Постараюсь тебя не разочаровать!\uD83D\uDE09\n"),
  TO_DO_LIST("Список дел"),
  ADD_NEW_TASK("Введите название задачи ⬇️\n"),
  EDIT_TASK_TITLE("Введите новое название задачи\n"),
  EDIT_TASK_DESCRIPTION("Введите описание задачи ⬇ \n"),
  TASK_DETAIL("Название задачи:    %s\n"
      + "Описание задачи:    %s\n"
      + "Статус по задаче:     %s\n\n"),
  STATUS_UPDATE_MESSAGE("Выберите новый статус задачи\n"),
  DELETE_OR_NOT("Вы точно хотите удалить задачу?\uD83E\uDDD0"),
  DELETE_TASK("Задача \"%s\" была удалена\n"),
  MAIN_MENU("Вы находитесь в главном меню бота. \n"
      + "Здесь можно выбрать одну из возможностей бота.");

  String message;

}
