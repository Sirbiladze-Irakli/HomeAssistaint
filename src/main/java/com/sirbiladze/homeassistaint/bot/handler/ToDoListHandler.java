package com.sirbiladze.homeassistaint.bot.handler;

import com.sirbiladze.homeassistaint.bot.keyboards.InlineKeyboardTaskDetailMaker;
import com.sirbiladze.homeassistaint.bot.keyboards.InlineKeyboardToDoListMaker;
import com.sirbiladze.homeassistaint.constants.BotMessageEnum;
import com.sirbiladze.homeassistaint.model.entity.TaskEntity;
import com.sirbiladze.homeassistaint.service.TaskService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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

  public SendMessage toDoListProcessing(CallbackQuery query) {
    String chatId = query.getMessage().getChatId().toString();
    String text = query.getMessage().getText();
    String userName = query.getFrom().getUserName();
    String data = query.getData();
    SendMessage sendMessage;

    if (data == null) {
      throw new IllegalArgumentException();
    }

    switch (data) {
      case ("newTask") :
        sendMessage = addNewTask(chatId);
        break;
      case ("changeStatus") :
        sendMessage = updateTaskStatus(chatId, text, userName);
        break;
      case ("delete") :
        sendMessage = deleteTask(chatId, text, userName);
        break;
      case ("back") :
        sendMessage = getTodoList(chatId, userName);
        break;
      default:
        sendMessage = getTaskDetail(chatId, data, userName);
    }
    return sendMessage;
  }

  private SendMessage updateTaskStatus(String chatId, String text, String userName) {
    String title = List.of(text.split("\n")).get(0);
    TaskEntity taskEntity = taskService.getTaskByTitleAndUserName(title, userName);
    SendMessage sendMessage =
        new SendMessage(chatId, BotMessageEnum.STATUS_UPDATE_MESSAGE.getMessage());
    sendMessage.setReplyMarkup(
        inlineKeyboardTaskDetailMaker.getInlineUpdateStatus(taskEntity.getStatus()));
    return sendMessage;
  }

  private SendMessage deleteTask(String chatId, String text, String userName) {
    String title = List.of(text.split("\n")).get(0);
    taskService.deleteTaskByTitleAndUserName(title, userName);
    SendMessage sendMessage = new SendMessage(chatId, getDeleteTaskText(title));
    List<TaskEntity> tasksFromDB = taskService.getAllTasksByUserName(userName);
    sendMessage.setReplyMarkup(
        inlineKeyboardToDoListMaker.getInlineKeyboardForTodoList(tasksFromDB));
    return sendMessage;
  }

  private SendMessage getTaskDetail(String chatId, String data, String userName) {
    TaskEntity taskEntityFromDB = taskService.getTaskByTitleAndUserName(data, userName);
    SendMessage sendMessage = new SendMessage(chatId, getTaskDetailText(taskEntityFromDB));
    sendMessage.setReplyMarkup(
        inlineKeyboardTaskDetailMaker.getInlineKeyboardForTaskDetail());
    return sendMessage;
  }

  public SendMessage getTodoList(String chatId, String userName) {
    List<TaskEntity> tasksFromDB = taskService.getAllTasksByUserName(userName);
    SendMessage sendMessage = new SendMessage(chatId, BotMessageEnum.TO_DO_LIST.getMessage());
    sendMessage.setReplyMarkup(
        inlineKeyboardToDoListMaker.getInlineKeyboardForTodoList(tasksFromDB));
    return sendMessage;
  }

  private SendMessage addNewTask(String chatId) {
    return new SendMessage(chatId, BotMessageEnum.HOW_TO_ADD_NEW_TASK.getMessage());
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

}
