package com.sirbiladze.homeassistaint.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum BotButtonTextEnum {

  TO_DO_LIST("✏ \uD83D\uDDD3 Список дел \uD83D\uDDD3 \uD83D\uDCCC");

  String buttonText;

}
