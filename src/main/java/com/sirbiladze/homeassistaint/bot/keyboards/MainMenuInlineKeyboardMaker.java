package com.sirbiladze.homeassistaint.bot.keyboards;

import com.sirbiladze.homeassistaint.model.constants.BotButtonTextEnum;
import com.sirbiladze.homeassistaint.model.constants.CallbackDataEnum;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class MainMenuInlineKeyboardMaker {

  public InlineKeyboardMarkup getMainMenuInlineKeyboard() {
    List<List<InlineKeyboardButton>> buttonList = new ArrayList<>();

    buttonList.add(getMainMenuRows());

    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    inlineKeyboardMarkup.setKeyboard(buttonList);
    return inlineKeyboardMarkup;
  }

  private List<InlineKeyboardButton> getMainMenuRows() {
    InlineKeyboardButton toDoList = new InlineKeyboardButton();
    toDoList.setText(BotButtonTextEnum.TO_DO_LIST.getButtonText());
    toDoList.setCallbackData(CallbackDataEnum.TO_DO_LIST.getCallbackData());

    return List.of(toDoList);
  }

}
