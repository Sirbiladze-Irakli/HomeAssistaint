package com.sirbiladze.homeassistaint.bot.handler;

import com.sirbiladze.homeassistaint.bot.cache.BotStateCache;
import com.sirbiladze.homeassistaint.bot.cache.TasksCache;
import com.sirbiladze.homeassistaint.constants.BotStateEnum;
import com.sirbiladze.homeassistaint.mapper.TaskMapper;
import com.sirbiladze.homeassistaint.model.Status;
import com.sirbiladze.homeassistaint.model.entity.TaskEntity;
import com.sirbiladze.homeassistaint.service.TaskService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BotStateHandler {

  CallbackQueryHandler callbackQueryHandler;
  TaskService taskService;
  BotStateCache botStateCache;
  TasksCache tasksCache;

  public SendMessage processBotState(EditMessageText editMessageText, String inputText,
      String userName, BotStateEnum botState) {
    if (botState == BotStateEnum.ADD_NEW_TASK) {
      TaskEntity taskEntity = TaskMapper.INSTANCE.map(
          inputText, "", editMessageText.getChatId(), userName, Status.TO_DO);
      taskService.saveTask(taskEntity);
      tasksCache.saveTask(editMessageText.getChatId(), taskEntity);
      botStateCache.saveBotState(editMessageText.getChatId(), BotStateEnum.NORMAL);
    }
    if (botState == BotStateEnum.EDIT_TASK_DESCRIPTION) {
      taskService.updateTaskDescription(
          tasksCache.getTasksMap().get(editMessageText.getChatId()).getTitle(),
          userName, inputText);
      tasksCache.getTasksMap().get(editMessageText.getChatId()).setDescription(inputText);
      botStateCache.saveBotState(editMessageText.getChatId(), BotStateEnum.NORMAL);
    }
    if (botState == BotStateEnum.EDIT_TASK_TITLE) {
      taskService.updateTaskTitle(
          tasksCache.getTasksMap().get(editMessageText.getChatId()).getTitle(),
          inputText, userName);
      tasksCache.getTasksMap().get(editMessageText.getChatId()).setTitle(inputText);
      botStateCache.saveBotState(editMessageText.getChatId(), BotStateEnum.NORMAL);
    }
    callbackQueryHandler.getTaskDetail(
        editMessageText, tasksCache.getTasksMap().get(editMessageText.getChatId()).getTitle(), userName);
    SendMessage sendMessage =
        new SendMessage(editMessageText.getChatId(), editMessageText.getText());
    sendMessage.setReplyMarkup(editMessageText.getReplyMarkup());
    return sendMessage;
  }
}
