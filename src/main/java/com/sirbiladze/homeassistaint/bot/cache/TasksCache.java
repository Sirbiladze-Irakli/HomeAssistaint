package com.sirbiladze.homeassistaint.bot.cache;

import com.sirbiladze.homeassistaint.model.entity.TaskEntity;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

/**
 * Кэш создан для хранения последней задачи из списка дел
 * с которой работает или работа клиент. Нужен чтобы случайно
 * не редактировать не ту запись.
 */

@Service
@Getter
@Setter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TasksCache {

  Map<String, TaskEntity> tasksMap = new HashMap<>();

  public void saveTask(String chatId, TaskEntity task) {
    tasksMap.put(chatId, task);
  }

}
