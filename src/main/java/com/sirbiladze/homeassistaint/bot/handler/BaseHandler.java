package com.sirbiladze.homeassistaint.bot.handler;

import com.sirbiladze.homeassistaint.bot.cache.BotStateCache;
import com.sirbiladze.homeassistaint.bot.cache.TasksCache;
import com.sirbiladze.homeassistaint.bot.keyboards.InlineKeyboardTaskDetailMaker;
import com.sirbiladze.homeassistaint.mapper.BotApiMethodMapper;
import com.sirbiladze.homeassistaint.model.constants.BotMessageEnum;
import com.sirbiladze.homeassistaint.model.entity.TaskEntity;
import com.sirbiladze.homeassistaint.service.TaskService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Component
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public abstract class BaseHandler {

  TaskService taskService;
  InlineKeyboardTaskDetailMaker inlineKeyboardTaskDetailMaker;

  public void isEmpty(String data) {
    if (data == null) {
      throw new IllegalArgumentException();
    }
  }

  public EditMessageText getTaskDetail(String chatId, Integer messageId,
      String title, String userName) {
    TaskEntity taskEntityFromDB = taskService.getTaskByTitleAndUserName(title, userName);
    return BotApiMethodMapper.INSTANCE.map(chatId, messageId, getTaskDetailText(taskEntityFromDB),
        inlineKeyboardTaskDetailMaker.getInlineKeyboardForTaskDetail(chatId));
  }

  public String getTaskDetailText(TaskEntity taskEntityFromDB) {
    return String.format(BotMessageEnum.TASK_DETAIL.getMessage(),
        taskEntityFromDB.getTitle(),
        taskEntityFromDB.getDescription(),
        taskEntityFromDB.getStatus().getText());
  }

}
