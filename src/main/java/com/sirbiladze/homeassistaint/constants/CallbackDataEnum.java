package com.sirbiladze.homeassistaint.constants;

import static org.apache.commons.lang3.EnumUtils.getEnumList;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.EnumUtils;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum CallbackDataEnum {

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
  BACK_TO_TO_DO_LIST("backToToDoList");

  String callbackData;

}
