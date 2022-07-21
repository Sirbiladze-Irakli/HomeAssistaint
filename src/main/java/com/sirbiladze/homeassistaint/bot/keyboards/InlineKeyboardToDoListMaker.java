package com.sirbiladze.homeassistaint.bot.keyboards;

import com.sirbiladze.homeassistaint.constants.BotButtonTextEnum;
import com.sirbiladze.homeassistaint.constants.CallbackDataEnum;
import com.sirbiladze.homeassistaint.model.Status;
import com.sirbiladze.homeassistaint.model.entity.TaskEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InlineKeyboardToDoListMaker {

  List<String> toDoList;
  List<String> inProgressList;
  List<String> doneList;
  int toDoSize;
  int inProgressSize;
  int doneSize;
  int biggestSize;

  public InlineKeyboardMarkup getInlineKeyboardForTodoList(List<TaskEntity> taskEntities) {
    return getInlineMessageButtons(taskEntities);
  }

  private InlineKeyboardMarkup getInlineMessageButtons(List<TaskEntity> taskEntities) {
    List<List<InlineKeyboardButton>> buttonList = new ArrayList<>();
    buttonList.add(getHeaderRow());
    fillTaskListsByStatus(taskEntities);
    for (int i = 0; i < biggestSize; i++) {
      buttonList.add(getTasksRow(i));
    }
    buttonList.add(getNewTaskRow());

    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    inlineKeyboardMarkup.setKeyboard(buttonList);
    return inlineKeyboardMarkup;
  }

  private void fillTaskListsByStatus(List<TaskEntity> taskEntities) {
    toDoList = new ArrayList<>();
    inProgressList = new ArrayList<>();
    doneList = new ArrayList<>();
    toDoSize = 0;
    inProgressSize = 0;
    doneSize = 0;
    biggestSize = 0;
    taskEntities.forEach(taskEntity -> {
      if (taskEntity.getStatus().equals(Status.TO_DO)) {
        toDoList.add(taskEntity.getTitle());
        toDoSize++;
        if (biggestSize < toDoSize) {
          biggestSize = toDoSize;
        }
      } else if (taskEntity.getStatus().equals(Status.IN_PROGRESS)) {
        inProgressList.add(taskEntity.getTitle());
        inProgressSize++;
        if (biggestSize < inProgressSize) {
          biggestSize = inProgressSize;
        }
      } else {
        doneList.add(taskEntity.getTitle());
        doneSize++;
        if (biggestSize < doneSize) {
          biggestSize = doneSize;
        }
      }
    });
  }

  private List<InlineKeyboardButton> getTasksRow(int i) {
    List<InlineKeyboardButton> tasksRow = new ArrayList<>();
    InlineKeyboardButton todoButton = new InlineKeyboardButton();
    InlineKeyboardButton inProgressButton = new InlineKeyboardButton();
    InlineKeyboardButton doneButton = new InlineKeyboardButton();
    if (!toDoList.isEmpty() && toDoSize > i) {
      todoButton.setText(toDoList.get(i));
      todoButton.setCallbackData(toDoList.get(i));
      tasksRow.add(todoButton);
    } else {
      todoButton.setText(BotButtonTextEnum.EMPTY_TASK.getButtonText());
      todoButton.setCallbackData(BotButtonTextEnum.EMPTY_TASK.getButtonText());
      tasksRow.add(todoButton);
    }
    if (!inProgressList.isEmpty() && inProgressSize > i) {
      inProgressButton.setText(inProgressList.get(i));
      inProgressButton.setCallbackData(inProgressList.get(i));
      tasksRow.add(inProgressButton);
    } else {
      inProgressButton.setText(BotButtonTextEnum.EMPTY_TASK.getButtonText());
      inProgressButton.setCallbackData(BotButtonTextEnum.EMPTY_TASK.getButtonText());
      tasksRow.add(inProgressButton);
    }
    if (!doneList.isEmpty() && doneSize > i) {
      doneButton.setText(doneList.get(i));
      doneButton.setCallbackData(doneList.get(i));
      tasksRow.add(doneButton);
    } else {
      doneButton.setText(BotButtonTextEnum.EMPTY_TASK.getButtonText());
      doneButton.setCallbackData(BotButtonTextEnum.EMPTY_TASK.getButtonText());
      tasksRow.add(doneButton);
    }
    return tasksRow;
  }

  private List<InlineKeyboardButton> getHeaderRow() {
    InlineKeyboardButton toDo = new InlineKeyboardButton();
    toDo.setText(Status.TO_DO_HEADER.getText());
    toDo.setCallbackData(CallbackDataEnum.NONE.getCallbackData());

    InlineKeyboardButton inProgress = new InlineKeyboardButton();
    inProgress.setText(Status.IN_PROGRESS_HEADER.getText());
    inProgress.setCallbackData(CallbackDataEnum.NONE.getCallbackData());

    InlineKeyboardButton done = new InlineKeyboardButton();
    done.setText(Status.DONE_HEADER.getText());
    done.setCallbackData(CallbackDataEnum.NONE.getCallbackData());

    return List.of(toDo, inProgress, done);
  }

  private List<InlineKeyboardButton> getNewTaskRow() {
    InlineKeyboardButton newTask = new InlineKeyboardButton();
    newTask.setText(BotButtonTextEnum.ADD_NEW_TASK.getButtonText());
    newTask.setCallbackData(CallbackDataEnum.ADD_NEW_TASK.getCallbackData());
    return List.of(newTask);
  }

}
