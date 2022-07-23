package com.sirbiladze.homeassistaint.bot.handler;

import com.sirbiladze.homeassistaint.bot.cache.BotStateCache;
import com.sirbiladze.homeassistaint.bot.cache.InterfaceMessageCache;
import com.sirbiladze.homeassistaint.bot.cache.TasksCache;
import com.sirbiladze.homeassistaint.bot.keyboards.InlineKeyboardTaskDetailMaker;
import com.sirbiladze.homeassistaint.mapper.TaskMapper;
import com.sirbiladze.homeassistaint.model.constants.BotCommandsEnum;
import com.sirbiladze.homeassistaint.model.constants.BotStateEnum;
import com.sirbiladze.homeassistaint.model.constants.Status;
import com.sirbiladze.homeassistaint.model.entity.TaskEntity;
import com.sirbiladze.homeassistaint.service.TaskService;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MessageHandler extends BaseHandler {

  MainMenuHandler mainMenuHandler;
  BotStateCache botStateCache;
  TasksCache tasksCache;
  InterfaceMessageCache interfaceMessageCache;

  public MessageHandler(TaskService taskService,
      InlineKeyboardTaskDetailMaker inlineKeyboardTaskDetailMaker,
      MainMenuHandler mainMenuHandler,
      BotStateCache botStateCache, TasksCache tasksCache,
      InterfaceMessageCache interfaceMessageCache) {
    super(taskService, inlineKeyboardTaskDetailMaker);
    this.mainMenuHandler = mainMenuHandler;
    this.botStateCache = botStateCache;
    this.tasksCache = tasksCache;
    this.interfaceMessageCache = interfaceMessageCache;
  }

  public List<BotApiMethod<?>> processMessage(Message message) {
    String chatId = message.getChatId().toString();
    List<BotApiMethod<?>> methods;

    if (isNoMessage(message)) {
      return List.of(new DeleteMessage(chatId, message.getMessageId()));
    }
    isEmpty(message.getText());

    if (message.getText().equals(BotCommandsEnum.START.getCommand())) {
      botStateCache.saveBotState(chatId, BotStateEnum.NORMAL);
      methods = mainMenuHandler.getMainMenuFromStart(message);
    } else if (botStateCache.getBotStateMap().get(chatId) == null ||
        botStateCache.getBotStateMap().get(chatId) == BotStateEnum.NORMAL) {
      methods = List.of(deleteMessage(chatId, message.getMessageId()));
    } else {
      methods = checkBotState(chatId, message);
    }

    return methods;
  }

  private List<BotApiMethod<?>> checkBotState(String chatId, Message message) {
    BotStateEnum botState = botStateCache.getBotStateMap().get(chatId);
    String userName = message.getFrom().getUserName();
    List<BotApiMethod<?>> methods;

    switch (botState) {
      case ADD_NEW_TASK:
        TaskEntity taskEntity = TaskMapper.INSTANCE.map(message.getText(), "",
            chatId, message.getFrom().getUserName(), Status.TO_DO);
        getTaskService().saveTask(taskEntity);
        tasksCache.saveTask(chatId, taskEntity);
        break;
      case EDIT_TASK_DESCRIPTION:
        getTaskService().updateTaskDescription(tasksCache.getTasksMap().get(chatId).getTitle(),
            userName, message.getText());
        tasksCache.getTasksMap().get(chatId).setDescription(message.getText());
        break;
      case EDIT_TASK_TITLE:
        getTaskService().updateTaskTitle(tasksCache.getTasksMap().get(chatId).getTitle(),
            message.getText(), userName);
        tasksCache.getTasksMap().get(chatId).setTitle(message.getText());
        break;
      default:
        //todo fixme
    }
    botStateCache.saveBotState(chatId, BotStateEnum.NORMAL);

    EditMessageText editMessageText = getTaskDetail(chatId,
        interfaceMessageCache.getBotStateMap().get(chatId),
        tasksCache.getTasksMap().get(chatId).getTitle(), userName);
    methods = List.of(editMessageText, deleteMessage(chatId, message.getMessageId()));
    return methods;
  }

  private boolean isNoMessage(Message message) {
    return message.hasSticker() || message.hasAnimation()
        || message.hasAudio() || message.hasPhoto() || message.hasVoice();
  }

  private BotApiMethod<?> deleteMessage(String chatId, Integer messageId) {
    return new DeleteMessage(chatId, messageId);
  }

}
