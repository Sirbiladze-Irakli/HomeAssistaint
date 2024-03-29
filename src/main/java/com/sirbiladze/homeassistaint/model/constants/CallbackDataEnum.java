package com.sirbiladze.homeassistaint.model.constants;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum CallbackDataEnum {

  TO_DO_LIST("toDoList"),
  TO_DO("toDo"),
  IN_PROGRESS("inProgress"),
  DONE("done"),
  NONE("➖"),
  ADD_NEW_TASK("addNewTask"),
  EDIT_TASK_TITLE("editTaskTitle"),
  ADD_DESCRIPTION("addDescription"),
  EDIT_DESCRIPTION("editDescription"),
  CHANGE_STATUS("changeStatus"),
  DELETE_OR_NOT("deleteOrNot"),
  DELETE("delete"),
  BACK_TO_TASK_DETAIL("backToTaskDetail"),
  BACK_TO_TO_DO_LIST("backToToDoList"),
  BACK_TO_MAIN_MENU("backToMainMenu");

  String callbackData;

}
