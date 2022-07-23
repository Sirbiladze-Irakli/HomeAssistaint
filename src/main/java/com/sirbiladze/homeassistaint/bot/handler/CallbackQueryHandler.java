package com.sirbiladze.homeassistaint.bot.handler;

import com.sirbiladze.homeassistaint.bot.cache.BotStateCache;
import com.sirbiladze.homeassistaint.bot.cache.InterfaceMessageCache;
import com.sirbiladze.homeassistaint.bot.cache.TasksCache;
import com.sirbiladze.homeassistaint.bot.keyboards.InlineKeyboardTaskDetailMaker;
import com.sirbiladze.homeassistaint.bot.keyboards.InlineKeyboardToDoListMaker;
import com.sirbiladze.homeassistaint.mapper.BotApiMethodMapper;
import com.sirbiladze.homeassistaint.model.constants.BotMessageEnum;
import com.sirbiladze.homeassistaint.model.constants.BotStateEnum;
import com.sirbiladze.homeassistaint.model.constants.Status;
import com.sirbiladze.homeassistaint.model.entity.TaskEntity;
import com.sirbiladze.homeassistaint.service.TaskService;
import com.sirbiladze.homeassistaint.utils.CallbackDataEnumUtils;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CallbackQueryHandler extends BaseHandler {

  InlineKeyboardToDoListMaker inlineKeyboardToDoListMaker;
  InlineKeyboardTaskDetailMaker inlineKeyboardTaskDetailMaker;
  MainMenuHandler mainMenuHandler;
  BotStateCache botStateCache;
  TasksCache tasksCache;
  InterfaceMessageCache interfaceMessageCache;

  public CallbackQueryHandler(TaskService taskService,
      InlineKeyboardTaskDetailMaker inlineKeyboardTaskDetailMaker,
      InlineKeyboardToDoListMaker inlineKeyboardToDoListMaker,
      InlineKeyboardTaskDetailMaker inlineKeyboardTaskDetailMaker1,
      MainMenuHandler mainMenuHandler,
      BotStateCache botStateCache, TasksCache tasksCache,
      InterfaceMessageCache interfaceMessageCache) {
    super(taskService, inlineKeyboardTaskDetailMaker);
    this.inlineKeyboardToDoListMaker = inlineKeyboardToDoListMaker;
    this.inlineKeyboardTaskDetailMaker = inlineKeyboardTaskDetailMaker1;
    this.mainMenuHandler = mainMenuHandler;
    this.botStateCache = botStateCache;
    this.tasksCache = tasksCache;
    this.interfaceMessageCache = interfaceMessageCache;
  }

  public List<BotApiMethod<?>> toDoListProcessing(Message message, CallbackQuery query) {

    EditMessageText editMessageText = new EditMessageText();
    if (message != null && message.getText().equals("/start")) {
      SendMessage sendMessage = new SendMessage(message.getChatId().toString(),
          BotMessageEnum.TO_DO_LIST.getMessage());
//      getTodoList(editMessageText.getChatId(),  message.getChat().getUserName());
      sendMessage.setReplyMarkup(editMessageText.getReplyMarkup());
      DeleteMessage deleteMessage =
          new DeleteMessage(message.getChatId().toString(), message.getMessageId());
      return List.of(sendMessage, deleteMessage);
    } else {
//      DeleteMessage deleteMessage =
//          new DeleteMessage(message.getChatId().toString(), message.getMessageId()-2);
      DeleteMessage deleteMessage1 =
          new DeleteMessage(message.getChatId().toString(), message.getMessageId());
      return List.of(deleteMessage1);
    }

//    BotStateEnum botState = botStateCache.getBotStateMap().get(message != null ?
//        message.getChatId().toString() : query.getMessage().getChatId().toString());

//    if (botState != null && botState != BotStateEnum.NORMAL) {
//      return botStateHandler.processBotState(editMessageText, message.getText(),
//          message.getChat().getUserName(), botState);
//    }
  }

  public List<BotApiMethod<?>> processQuery(CallbackQuery query) {
    String data = query.getData();
    String chatId = query.getMessage().getChatId().toString();
    Integer messageId = query.getMessage().getMessageId();
    String userName = query.getFrom().getUserName();
    List<BotApiMethod<?>> methods;

    isEmpty(query.getData());
    if (!CallbackDataEnumUtils.checkEnum(data)) {
      tasksCache.saveTask(chatId, getTaskService().getTaskByTitleAndUserName(data, userName));
    }

    switch (data) {
      case ("toDoList"):
      case ("backToToDoList"):
        methods = getTodoList(chatId, messageId, userName);
        botStateCache.saveBotState(chatId, BotStateEnum.NORMAL);
        interfaceMessageCache.saveInterfaceMessage(chatId, messageId);
        break;
      case ("addNewTask"):
        methods = List.of(newTaskMessage(chatId, messageId,
            BotMessageEnum.ADD_NEW_TASK.getMessage()));
        botStateCache.saveBotState(chatId, BotStateEnum.ADD_NEW_TASK);
        break;
      case ("editTaskTitle"):
        methods = List.of(newTaskMessage(chatId, messageId,
            BotMessageEnum.EDIT_TASK_TITLE.getMessage()));
        botStateCache.saveBotState(chatId, BotStateEnum.EDIT_TASK_TITLE);
        break;
      case ("addDescription") :
      case ("editDescription") :
        methods = List.of(newTaskMessage(chatId, messageId,
            BotMessageEnum.EDIT_TASK_DESCRIPTION.getMessage()));
        botStateCache.saveBotState(chatId, BotStateEnum.EDIT_TASK_DESCRIPTION);
        break;
      case ("changeStatus") :
        methods = List.of(getUpdateTaskStatusKeyboard(chatId, messageId));
        break;
      case ("toDo") :
        methods = updateTaskStatus(chatId, messageId, userName, Status.TO_DO);
        break;
      case ("inProgress") :
        methods = updateTaskStatus(chatId, messageId, userName, Status.IN_PROGRESS);
        break;
      case ("done") :
        methods = updateTaskStatus(chatId, messageId, userName, Status.DONE);
        break;
      case ("deleteOrNot"):
        methods = List.of(deleteTaskOrNot(chatId, messageId));
        break;
//      case ("backToTaskDetail") :
//        methods = List.of(getTaskDetail(chatId, messageId,
//            tasksCache.getTasksMap().get(chatId).getTitle(), userName));
//        break;
      case ("delete") :
        methods = List.of(deleteTask(chatId, messageId, userName));
        break;
      case ("backToMainMenu"):
        methods = mainMenuHandler.backToMainMenu(chatId, messageId);
        break;
      default:
        methods = List.of(getTaskDetail(chatId, messageId,
            tasksCache.getTasksMap().get(chatId).getTitle(), userName));
    }
    return methods;
  }

  private EditMessageText deleteTaskOrNot(String chatId, Integer messageId) {
    return BotApiMethodMapper.INSTANCE
        .map(
            chatId,
            messageId,
            BotMessageEnum.DELETE_OR_NOT.getMessage(),
            inlineKeyboardTaskDetailMaker.getInlineDeleteButtons()
        );
  }

  private EditMessageText getUpdateTaskStatusKeyboard(String chatId, Integer messageId) {
    TaskEntity taskEntity = tasksCache.getTasksMap().get(chatId);
    String text = String.format(BotMessageEnum.TASK_DETAIL.getMessage()
            .concat(BotMessageEnum.STATUS_UPDATE_MESSAGE.getMessage()),
        taskEntity.getTitle(), taskEntity.getDescription(), taskEntity.getStatus().getText());
    InlineKeyboardMarkup inlineKeyboardMarkup =
        inlineKeyboardTaskDetailMaker.getInlineUpdateStatus(taskEntity.getStatus());
    return BotApiMethodMapper.INSTANCE.map(chatId, messageId, text, inlineKeyboardMarkup);
  }

  public List<BotApiMethod<?>> getTodoList(String chatId, Integer messageId, String userName) {
    List<TaskEntity> tasksFromDB = getTaskService().getAllTasksByUserName(userName);
    return List.of(BotApiMethodMapper.INSTANCE
        .map(chatId,
            messageId,
            BotMessageEnum.TO_DO_LIST.getMessage(),
            inlineKeyboardToDoListMaker.getInlineKeyboardForTodoList(tasksFromDB)
        ));
  }

  private String getDeleteTaskText(String title) {
    return String.format(BotMessageEnum.DELETE_TASK.getMessage(), title);
  }

  private List<BotApiMethod<?>> updateTaskStatus(
      String chatId, Integer messageId, String userName, Status status) {
    String title = tasksCache.getTasksMap().get(chatId).getTitle();
    getTaskService().updateTaskStatus(title, userName, status);
    return getTodoList(chatId, messageId, userName);
  }

  private EditMessageText deleteTask(String chatId, Integer messageId, String userName) {
    String title = tasksCache.getTasksMap().get(chatId).getTitle();
    getTaskService().deleteTaskByTitleAndUserName(title, userName);
    List<TaskEntity> tasksFromDB = getTaskService().getAllTasksByUserName(userName);

    tasksCache.getTasksMap().remove(chatId);
    return BotApiMethodMapper.INSTANCE
        .map(
            chatId,
            messageId,
            getDeleteTaskText(title),
            inlineKeyboardToDoListMaker.getInlineKeyboardForTodoList(tasksFromDB)
        );
  }

  private EditMessageText newTaskMessage(String chatId, Integer messageId,
      String botMessage) {
    return BotApiMethodMapper.INSTANCE.map(chatId, messageId, botMessage,
        getInlineKeyboardTaskDetailMaker().setBackToToDoListButton());
  }


}
