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

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BotStateHandler {

  ToDoListHandler toDoListHandler;
  TaskService taskService;
  BotStateCache botStateCache;
  TasksCache tasksCache;

  public SendMessage processBotState(String chatId, String inputText, String userName,
      BotStateEnum botState) {
    if (botState == BotStateEnum.ADD_NEW_TASK) {
      TaskEntity taskEntity = TaskMapper.INSTANCE.map(
          inputText, "", chatId, userName, Status.TO_DO);
      taskService.saveTask(taskEntity);
      tasksCache.saveTask(chatId, taskEntity);
      botStateCache.saveBotState(chatId, BotStateEnum.NORMAL);
    }
    if (botState == BotStateEnum.EDIT_TASK_DESCRIPTION) {
      taskService.updateTaskDescription(
          tasksCache.getTasksMap().get(chatId).getTitle(), userName, inputText);
      tasksCache.getTasksMap().get(chatId).setDescription(inputText);
      botStateCache.saveBotState(chatId, BotStateEnum.NORMAL);
    }
    if (botState == BotStateEnum.EDIT_TASK_TITLE) {
      taskService.updateTaskTitle(
          tasksCache.getTasksMap().get(chatId).getTitle(), inputText, userName);
      tasksCache.getTasksMap().get(chatId).setTitle(inputText);
      botStateCache.saveBotState(chatId, BotStateEnum.NORMAL);
    }
    return toDoListHandler.getTaskDetail(
        chatId, tasksCache.getTasksMap().get(chatId).getTitle(), userName);
  }
}
