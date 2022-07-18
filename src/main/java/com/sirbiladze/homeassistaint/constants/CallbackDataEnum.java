package com.sirbiladze.homeassistaint.constants;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum CallbackDataEnum {

  TO_DO("toDo"),
  IN_PROGRESS("inProgress"),
  DONE("done"),
  NONE("➖"),
  NEW_TASK("newTask"),
  CHANGE_STATUS("changeStatus"),
  DELETE("delete"),
  BACK("back");

  String callbackData;

}
