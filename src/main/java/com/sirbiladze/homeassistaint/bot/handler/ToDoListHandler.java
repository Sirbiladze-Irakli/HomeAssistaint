package com.sirbiladze.homeassistaint.bot.handler;

import static com.sirbiladze.homeassistaint.utils.BotAnswerUtils.getRandomException;

import com.sirbiladze.homeassistaint.bot.cache.BotStateCache;
import com.sirbiladze.homeassistaint.bot.cache.TasksCache;
import com.sirbiladze.homeassistaint.bot.keyboards.InlineKeyboardTaskDetailMaker;
import com.sirbiladze.homeassistaint.bot.keyboards.InlineKeyboardToDoListMaker;
import com.sirbiladze.homeassistaint.constants.BotMessageEnum;
import com.sirbiladze.homeassistaint.constants.BotStateEnum;
import com.sirbiladze.homeassistaint.model.Status;
import com.sirbiladze.homeassistaint.model.entity.TaskEntity;
import com.sirbiladze.homeassistaint.service.TaskService;
import com.sirbiladze.homeassistaint.utils.CallbackDataEnumUtils;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ToDoListHandler {

  InlineKeyboardToDoListMaker inlineKeyboardToDoListMaker;
  InlineKeyboardTaskDetailMaker inlineKeyboardTaskDetailMaker;
  TaskService taskService;
  BotStateCache botStateCache;
  TasksCache tasksCache;
  BotStateHandler botStateHandler;

  public BotApiMethod<?> toDoListProcessing(
      Message message, CallbackQuery query) {

    EditMessageText editMessageText = new EditMessageText();
    if (message != null && message.getText().equals("/start")) {
      SendMessage sendMessage = new SendMessage(message.getChatId().toString(),
          BotMessageEnum.TO_DO_LIST.getMessage());
      getTodoList(editMessageText, message.getChat().getUserName());
      sendMessage.setReplyMarkup(editMessageText.getReplyMarkup());
      return sendMessage;
    }

    BotStateEnum botState = botStateCache.getBotStateMap().get(message != null ?
        message.getChatId().toString() : query.getMessage().getChatId().toString());

    if (botState != null && botState != BotStateEnum.NORMAL) {
      return botStateHandler.processBotState(editMessageText, message.getText(),
          message.getChat().getUserName(), botState);
    }

    String chatId = query.getMessage().getChatId().toString();
    String userName = query.getFrom().getUserName();
    String data = query.getData();
    SendMessage sendMessage;
    editMessageText.setChatId(chatId);
    editMessageText.setMessageId(query.getMessage().getMessageId());

    if (data == null) {
      throw new IllegalArgumentException();
    }

    if (!CallbackDataEnumUtils.checkEnum(data)) {
      tasksCache.saveTask(chatId, taskService.getTaskByTitleAndUserName(data, userName));
    }

    switch (data) {
      case ("addNewTask") :
//        sendMessage = taskMessage(chatId, BotMessageEnum.ADD_NEW_TASK.getMessage());
        editMessageText.setText(BotMessageEnum.ADD_NEW_TASK.getMessage());
        botStateCache.saveBotState(chatId, BotStateEnum.ADD_NEW_TASK);
        break;
      case ("editTaskTitle"):
        if (tasksCache.getTasksMap().get(chatId) == null) {
          return new SendMessage(chatId, getRandomException());
        }
//        sendMessage = taskMessage(chatId, BotMessageEnum.EDIT_TASK_TITLE.getMessage());
        editMessageText.setText(BotMessageEnum.EDIT_TASK_TITLE.getMessage());
        botStateCache.saveBotState(chatId, BotStateEnum.EDIT_TASK_TITLE);
        break;
      case ("addDescription") :
      case ("editDescription") :
        if (tasksCache.getTasksMap().get(chatId) == null) {
          return new SendMessage(chatId, getRandomException());
        }
//        sendMessage = taskMessage(chatId, BotMessageEnum.EDIT_TASK_DESCRIPTION.getMessage());
        editMessageText.setText(BotMessageEnum.EDIT_TASK_DESCRIPTION.getMessage());
        botStateCache.saveBotState(chatId, BotStateEnum.EDIT_TASK_DESCRIPTION);
        break;
      case ("changeStatus") :
        if (tasksCache.getTasksMap().get(chatId) == null) {
          return new SendMessage(chatId, getRandomException());
        }
//        sendMessage = getUpdateTaskStatusKeyboard(chatId);
        getUpdateTaskStatusKeyboard(editMessageText);
        break;
      case ("toDo") :
//        sendMessage = updateTaskStatus(chatId, userName, Status.TO_DO);
        updateTaskStatus(editMessageText, userName, Status.TO_DO);
        break;
      case ("inProgress") :
//        sendMessage = updateTaskStatus(chatId, userName, Status.IN_PROGRESS);
        updateTaskStatus(editMessageText, userName, Status.IN_PROGRESS);
        break;
      case ("done") :
//        sendMessage = updateTaskStatus(chatId, userName, Status.DONE);
        updateTaskStatus(editMessageText, userName, Status.DONE);
        break;
      case ("deleteOrNot"):
        if (tasksCache.getTasksMap().get(chatId) == null) {
          return new SendMessage(chatId, getRandomException());
        }
//        sendMessage = deleteTaskOrNot(chatId);
        deleteTaskOrNot(editMessageText);
        break;
      case ("backToTaskDetail") :
//        sendMessage =
//            getTaskDetail(chatId, tasksCache.getTasksMap().get(chatId).getTitle(), userName);
        getTaskDetail(editMessageText, tasksCache.getTasksMap().get(chatId).getTitle(), userName);
        break;
      case ("delete") :
//        sendMessage = deleteTask(chatId, userName);
        deleteTask(editMessageText, userName);
        break;
      case ("backToToDoList") :
//        sendMessage = getTodoList(chatId, userName);
        getTodoList(editMessageText, userName);
        break;
      default:
//        sendMessage = getTaskDetail(chatId, data, userName);
        getTaskDetail(editMessageText, data, userName);
    }
    return editMessageText;
  }

  private void deleteTaskOrNot(EditMessageText editMessageText) {
    editMessageText.setText(BotMessageEnum.DELETE_OR_NOT.getMessage());
    editMessageText.setReplyMarkup(
        inlineKeyboardTaskDetailMaker.getInlineDeleteButtons());
//    SendMessage sendMessage = new SendMessage(chatId, BotMessageEnum.DELETE_OR_NOT.getMessage());
//    sendMessage.setReplyMarkup(
//        inlineKeyboardTaskDetailMaker.getInlineDeleteButtons());
//    return sendMessage;
  }

  private void getUpdateTaskStatusKeyboard(EditMessageText editMessageText) {
    TaskEntity taskEntity = tasksCache.getTasksMap().get(editMessageText.getChatId());
    editMessageText.setText(String.format(BotMessageEnum.TASK_DETAIL.getMessage()
        .concat(BotMessageEnum.STATUS_UPDATE_MESSAGE.getMessage()),
        taskEntity.getTitle(), taskEntity.getDescription(), taskEntity.getStatus().getText()));
    editMessageText.setReplyMarkup(
        inlineKeyboardTaskDetailMaker.getInlineUpdateStatus(taskEntity.getStatus()));
//    SendMessage sendMessage =
//        new SendMessage(chatId, String.format(BotMessageEnum.TASK_DETAIL.getMessage()
//                .concat(BotMessageEnum.STATUS_UPDATE_MESSAGE.getMessage()),
//            taskEntity.getTitle(), taskEntity.getDescription(), taskEntity.getStatus().getText()));
//    sendMessage.setReplyMarkup(
//        inlineKeyboardTaskDetailMaker.getInlineUpdateStatus(taskEntity.getStatus()));
//    return sendMessage;
  }

  public EditMessageText getTaskDetail(EditMessageText editMessageText, String title, String userName) {
    TaskEntity taskEntityFromDB = taskService.getTaskByTitleAndUserName(title, userName);
    editMessageText.setText(getTaskDetailText(taskEntityFromDB));
    editMessageText.setReplyMarkup(
        inlineKeyboardTaskDetailMaker.getInlineKeyboardForTaskDetail(editMessageText.getChatId()));
    return editMessageText;
//    SendMessage sendMessage = new SendMessage(chatId, getTaskDetailText(taskEntityFromDB));
//    sendMessage.setReplyMarkup(
//        inlineKeyboardTaskDetailMaker.getInlineKeyboardForTaskDetail(chatId));
//    return sendMessage;
  }

  public void getTodoList(EditMessageText editMessageText, String userName) {
    List<TaskEntity> tasksFromDB = taskService.getAllTasksByUserName(userName);
    editMessageText.setText(BotMessageEnum.TO_DO_LIST.getMessage());
    editMessageText.setReplyMarkup(
        inlineKeyboardToDoListMaker.getInlineKeyboardForTodoList(tasksFromDB));
//    SendMessage sendMessage = new SendMessage(chatId, BotMessageEnum.TO_DO_LIST.getMessage());
//    sendMessage.setReplyMarkup(
//        inlineKeyboardToDoListMaker.getInlineKeyboardForTodoList(tasksFromDB));
//    return sendMessage;
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

  private void updateTaskStatus(
      EditMessageText editMessageText, String userName, Status status) {
    String title = tasksCache.getTasksMap().get(editMessageText.getChatId()).getTitle();
    taskService.updateTaskStatus(title, userName, status);
//    return getTodoList(editMessageText, userName);
    getTodoList(editMessageText, userName);
  }

  private void deleteTask(EditMessageText editMessageText, String userName) {
    String title = tasksCache.getTasksMap().get(editMessageText.getChatId()).getTitle();
    taskService.deleteTaskByTitleAndUserName(title, userName);
    editMessageText.setText(getDeleteTaskText(title));
    List<TaskEntity> tasksFromDB = taskService.getAllTasksByUserName(userName);
    editMessageText.setReplyMarkup(
        inlineKeyboardToDoListMaker.getInlineKeyboardForTodoList(tasksFromDB));

//    SendMessage sendMessage = new SendMessage(chatId, getDeleteTaskText(title));
//    sendMessage.setReplyMarkup(
//        inlineKeyboardToDoListMaker.getInlineKeyboardForTodoList(tasksFromDB));
    tasksCache.getTasksMap().remove(editMessageText.getChatId());
//    return sendMessage;
  }

  private SendMessage taskMessage(String chatId, String botMessage) {
    return new SendMessage(chatId, botMessage);
  }

}
