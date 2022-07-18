package com.sirbiladze.homeassistaint.bot.keyboards;

import com.sirbiladze.homeassistaint.constants.BotButtonTextEnum;
import com.sirbiladze.homeassistaint.constants.CallbackDataEnum;
import com.sirbiladze.homeassistaint.model.Status;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InlineKeyboardTaskDetailMaker {

  public InlineKeyboardMarkup getInlineKeyboardForTaskDetail() {
    return getInlineMessageButtons();
  }

  private InlineKeyboardMarkup getInlineMessageButtons() {
    List<List<InlineKeyboardButton>> buttonList = new ArrayList<>();
    buttonList.add(getNewRowButton(BotButtonTextEnum.CHANGE_STATUS.getButtonText(),
        CallbackDataEnum.CHANGE_STATUS.getCallbackData()));
    buttonList.add(getNewRowButton(BotButtonTextEnum.DELETE.getButtonText(),
        CallbackDataEnum.DELETE.getCallbackData()));
    buttonList.add(getNewRowButton(BotButtonTextEnum.BACK.getButtonText(),
        CallbackDataEnum.BACK.getCallbackData()));

    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    inlineKeyboardMarkup.setKeyboard(buttonList);
    return inlineKeyboardMarkup;
  }

  private List<InlineKeyboardButton> getNewRowButton(String buttonText, String callBackData) {
    InlineKeyboardButton newButton = new InlineKeyboardButton();
    newButton.setText(buttonText);
    newButton.setCallbackData(callBackData);
    return List.of(newButton);
  }

  public InlineKeyboardMarkup getInlineUpdateStatus(Status status) {
    List<List<InlineKeyboardButton>> buttonList = new ArrayList<>();
    InlineKeyboardButton toDo = new InlineKeyboardButton();
    toDo.setText(status != Status.TO_DO ?
        Status.TO_DO.getText() : Status.NONE.getText());
    toDo.setCallbackData(status != Status.TO_DO ?
        CallbackDataEnum.TO_DO.getCallbackData() : CallbackDataEnum.NONE.getCallbackData());

    InlineKeyboardButton inProgress = new InlineKeyboardButton();
    inProgress.setText(status != Status.IN_PROGRESS ?
        Status.IN_PROGRESS.getText(): Status.NONE.getText());
    inProgress.setCallbackData(status != Status.IN_PROGRESS ?
        CallbackDataEnum.IN_PROGRESS.getCallbackData() : CallbackDataEnum.NONE.getCallbackData());

    InlineKeyboardButton done = new InlineKeyboardButton();
    done.setText(status != Status.DONE ? Status.DONE.getText(): Status.NONE.getText());
    done.setCallbackData(status != Status.DONE ?
        CallbackDataEnum.DONE.getCallbackData() : CallbackDataEnum.NONE.getCallbackData());

    buttonList.add(List.of(toDo, inProgress, done));
    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    inlineKeyboardMarkup.setKeyboard(buttonList);
    return inlineKeyboardMarkup;
  }
}
