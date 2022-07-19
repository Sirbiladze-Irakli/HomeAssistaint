package com.sirbiladze.homeassistaint.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public enum Status {
  TO_DO("К Выполнению"),
  TO_DO_HEADER("К Выполнению⬇️"),
  IN_PROGRESS("В работе"),
  IN_PROGRESS_HEADER("В работе⬇️"),
  DONE("Завершено"),
  DONE_HEADER("Завершено⬇️"),
  NONE("➖");

  String text;
}
