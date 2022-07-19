package com.sirbiladze.homeassistaint.bot.handler;

import com.sirbiladze.homeassistaint.bot.cache.BotStateCache;
import com.sirbiladze.homeassistaint.bot.cache.TasksCache;
import com.sirbiladze.homeassistaint.bot.keyboards.InlineKeyboardTaskDetailMaker;
import com.sirbiladze.homeassistaint.bot.keyboards.InlineKeyboardToDoListMaker;
import com.sirbiladze.homeassistaint.constants.BotMessageEnum;
import com.sirbiladze.homeassistaint.constants.BotStateEnum;
import com.sirbiladze.homeassistaint.constants.CallbackDataEnum;
import com.sirbiladze.homeassistaint.model.Status;
import com.sirbiladze.homeassistaint.model.entity.TaskEntity;
import com.sirbiladze.homeassistaint.service.TaskService;
import com.sirbiladze.homeassistaint.utils.CallbackDataEnumUtils;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ToDoListHandler {

  InlineKeyboardToDoListMaker inlineKeyboardToDoListMaker;
  InlineKeyboardTaskDetailMaker inlineKeyboardTaskDetailMaker;
  TaskService taskService;
  BotStateCache botStateCache;
  TasksCache tasksCache;

  public SendMessage toDoListProcessing(CallbackQuery query) {
    String chatId = query.getMessage().getChatId().toString();
    String userName = query.getFrom().getUserName();
    String data = query.getData();
    SendMessage sendMessage;

    if (data == null) {
      throw new IllegalArgumentException();
    }

    if (!CallbackDataEnumUtils.checkEnum(data)) {
      tasksCache.saveTask(chatId, taskService.getTaskByTitleAndUserName(data, userName));
    }

    switch (data) {
      case ("addNewTask") :
        sendMessage = taskMessage(chatId, BotMessageEnum.ADD_NEW_TASK.getMessage());
        botStateCache.saveBotState(chatId, BotStateEnum.ADD_NEW_TASK);
        break;
      case ("editTaskTitle"):
        sendMessage = taskMessage(chatId, BotMessageEnum.EDIT_TASK_TITLE.getMessage());
        botStateCache.saveBotState(chatId, BotStateEnum.EDIT_TASK_TITLE);
        break;
      case ("addDescription") :
      case ("editDescription") :
        sendMessage = taskMessage(chatId, BotMessageEnum.EDIT_TASK_DESCRIPTION.getMessage());
        botStateCache.saveBotState(chatId, BotStateEnum.EDIT_TASK_DESCRIPTION);
        break;
      case ("changeStatus") :
        sendMessage = getUpdateTaskStatusKeyboard(chatId);
        break;
      case ("toDo") :
        sendMessage = updateTaskStatus(chatId, userName, Status.TO_DO);
        break;
      case ("inProgress") :
        sendMessage = updateTaskStatus(chatId, userName, Status.IN_PROGRESS);
        break;
      case ("done") :
        sendMessage = updateTaskStatus(chatId, userName, Status.DONE);
        break;
      case ("deleteOrNot"):
        sendMessage = deleteTaskOrNot(chatId);
        break;
      case ("backToTaskDetail") :
        sendMessage =
            getTaskDetail(chatId, tasksCache.getTasksMap().get(chatId).getTitle(), userName);
        break;
      case ("delete") :
        sendMessage = deleteTask(chatId, userName);
        break;
      case ("backToToDoList") :
        sendMessage = getTodoList(chatId, userName);
        break;
      default:
        sendMessage = getTaskDetail(chatId, data, userName);
    }
    return sendMessage;
  }

  private SendMessage deleteTaskOrNot(String chatId) {
    SendMessage sendMessage = new SendMessage(chatId, BotMessageEnum.DELETE_OR_NOT.getMessage());
    sendMessage.setReplyMarkup(
        inlineKeyboardTaskDetailMaker.getInlineDeleteButtons());
    return sendMessage;
  }

  private SendMessage getUpdateTaskStatusKeyboard(String chatId) {
    TaskEntity taskEntity = tasksCache.getTasksMap().get(chatId);
    SendMessage sendMessage =
        new SendMessage(chatId, String.format(BotMessageEnum.TASK_DETAIL.getMessage()
                .concat(BotMessageEnum.STATUS_UPDATE_MESSAGE.getMessage()),
            taskEntity.getTitle(), taskEntity.getDescription(), taskEntity.getStatus().getText()));
    sendMessage.setReplyMarkup(
        inlineKeyboardTaskDetailMaker.getInlineUpdateStatus(taskEntity.getStatus()));
    return sendMessage;
  }

  public SendMessage getTaskDetail(String chatId, String title, String userName) {
    TaskEntity taskEntityFromDB = taskService.getTaskByTitleAndUserName(title, userName);
    SendMessage sendMessage = new SendMessage(chatId, getTaskDetailText(taskEntityFromDB));
    sendMessage.setReplyMarkup(
        inlineKeyboardTaskDetailMaker.getInlineKeyboardForTaskDetail(chatId));
    return sendMessage;
  }

  public SendMessage getTodoList(String chatId, String userName) {
    List<TaskEntity> tasksFromDB = taskService.getAllTasksByUserName(userName);
    SendMessage sendMessage = new SendMessage(chatId, BotMessageEnum.TO_DO_LIST.getMessage());
    sendMessage.setReplyMarkup(
        inlineKeyboardToDoListMaker.getInlineKeyboardForTodoList(tasksFromDB));
    return sendMessage;
  }

  private String getTaskDetailText(TaskEntity taskEntityFromDB) {
    return String.format(BotMessageEnum.TASK_DETAIL.getMessage(),
        taskEntityFromDB.getTitle(),
        taskEntityFromDB.getDescription(),
        taskEntityFromDB.getStatus().getText());
  }

  private String getDeleteTaskText(String title) {
    return String.format(BotMessageEnum.DELETE_TASK.getMessage(), title);
  }

  private SendMessage updateTaskStatus(
      String chatId, String userName, Status status) {
    String title = tasksCache.getTasksMap().get(chatId).getTitle();
    taskService.updateTaskStatus(title, userName, status);
    return getTodoList(chatId, userName);
  }

  private SendMessage deleteTask(String chatId, String userName) {
    String title = tasksCache.getTasksMap().get(chatId).getTitle();
    taskService.deleteTaskByTitleAndUserName(title, userName);
    SendMessage sendMessage = new SendMessage(chatId, getDeleteTaskText(title));
    List<TaskEntity> tasksFromDB = taskService.getAllTasksByUserName(userName);
    sendMessage.setReplyMarkup(
        inlineKeyboardToDoListMaker.getInlineKeyboardForTodoList(tasksFromDB));
    botStateCache.getBotStateMap().remove(chatId);
    return sendMessage;
  }

  private SendMessage taskMessage(String chatId, String botMessage) {
    return new SendMessage(chatId, botMessage);
  }

}
