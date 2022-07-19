package com.sirbiladze.homeassistaint.bot.keyboards;

import com.sirbiladze.homeassistaint.bot.cache.TasksCache;
import com.sirbiladze.homeassistaint.constants.BotButtonTextEnum;
import com.sirbiladze.homeassistaint.constants.CallbackDataEnum;
import com.sirbiladze.homeassistaint.model.Status;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InlineKeyboardTaskDetailMaker {

  TasksCache tasksCache;

  public InlineKeyboardMarkup getInlineKeyboardForTaskDetail(String chatId) {
    return getInlineMessageButtons(chatId);
  }

  private InlineKeyboardMarkup getInlineMessageButtons(String chatId) {
    List<List<InlineKeyboardButton>> buttonList = new ArrayList<>();
    buttonList.add(getNewRowButton(BotButtonTextEnum.EDIT_TASK_TITLE.getButtonText(),
        CallbackDataEnum.EDIT_TASK_TITLE.getCallbackData()));
    if (tasksCache.getTasksMap().get(chatId).getDescription().isEmpty()) {
      buttonList.add(getNewRowButton(BotButtonTextEnum.ADD_DESCRIPTION.getButtonText(),
          CallbackDataEnum.ADD_DESCRIPTION.getCallbackData()));
    } else {
      buttonList.add(getNewRowButton(BotButtonTextEnum.EDIT_DESCRIPTION.getButtonText(),
          CallbackDataEnum.EDIT_DESCRIPTION.getCallbackData()));
    }
    buttonList.add(getNewRowButton(BotButtonTextEnum.CHANGE_STATUS.getButtonText(),
        CallbackDataEnum.CHANGE_STATUS.getCallbackData()));
    buttonList.add(getNewRowButton(BotButtonTextEnum.DELETE_OR_NOT.getButtonText(),
        CallbackDataEnum.DELETE_OR_NOT.getCallbackData()));
    buttonList.add(getNewRowButton(BotButtonTextEnum.BACK_TO_TO_DO_LIST.getButtonText(),
        CallbackDataEnum.BACK_TO_TO_DO_LIST.getCallbackData()));

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

  public InlineKeyboardMarkup getInlineDeleteButtons() {
    List<List<InlineKeyboardButton>> buttonList = new ArrayList<>();
    InlineKeyboardButton backToTaskDetail = new InlineKeyboardButton();
    backToTaskDetail.setText(BotButtonTextEnum.BACK_TO_TASK_DETAIL.getButtonText());
    backToTaskDetail.setCallbackData(CallbackDataEnum.BACK_TO_TASK_DETAIL.getCallbackData());

    InlineKeyboardButton delete = new InlineKeyboardButton();
    delete.setText(BotButtonTextEnum.DELETE_TASK.getButtonText());
    delete.setCallbackData(CallbackDataEnum.DELETE.getCallbackData());

    buttonList.add(List.of(backToTaskDetail,delete));
    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    inlineKeyboardMarkup.setKeyboard(buttonList);
    return inlineKeyboardMarkup;
  }

}
