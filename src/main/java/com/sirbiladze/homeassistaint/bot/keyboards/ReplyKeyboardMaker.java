package com.sirbiladze.homeassistaint.bot.keyboards;

import static com.sirbiladze.homeassistaint.constants.BotButtonTextEnum.TO_DO_LIST;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Component
public class ReplyKeyboardMaker {

  public ReplyKeyboardMarkup getMainMenuKeyboard() {
    KeyboardRow row1 = new KeyboardRow();
    row1.add(new KeyboardButton(TO_DO_LIST.getButtonText()));

//    KeyboardRow row2 = new KeyboardRow();
//    row2.add(new KeyboardButton("Удалить дело"));
//    row2.add(new KeyboardButton("Выйти"));

    List<KeyboardRow> keyboard = new ArrayList<>();
    keyboard.add(row1);
//    keyboard.add(row2);

    final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    replyKeyboardMarkup.setKeyboard(keyboard);
    replyKeyboardMarkup.setSelective(true);
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setOneTimeKeyboard(false);

    return replyKeyboardMarkup;
  }

}
