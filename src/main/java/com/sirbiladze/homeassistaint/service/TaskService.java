package com.sirbiladze.homeassistaint.service;

import static java.lang.String.format;

import com.sirbiladze.homeassistaint.mapper.TaskMapper;
import com.sirbiladze.homeassistaint.model.Status;
import com.sirbiladze.homeassistaint.model.entity.TaskEntity;
import com.sirbiladze.homeassistaint.model.exception.NotFoundException;
import com.sirbiladze.homeassistaint.repository.TaskRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

  private final TaskRepository taskRepository;

  public TaskEntity getTaskByTitleAndUserName(String title, String userName) {
    log.debug("Try to find task with title {} and user name {}", title, userName);
    return taskRepository.findByTitleAndUserName(title, userName).orElseThrow(
        () -> new NotFoundException(
            format("Not found Task with title \"%s\" and user name \"%s\"", title, userName)
        ));
  }

  public TaskEntity getTask(Long id) {
    log.debug("Try to find task with id {}", id);
    return taskRepository.findById(id).orElseThrow(
        () -> new NotFoundException(format("Not found Task with id %d", id)));
  }

  public void saveTask(TaskEntity taskEntity) {
    taskRepository.save(taskEntity);
    log.debug("Save task {}", taskEntity);
  }

  public void saveNewTask(List<String> newTask, String chatId, String userName) {
    TaskEntity taskEntity =
        TaskMapper.INSTANCE.map(newTask.get(1), newTask.get(2), chatId, userName, Status.TO_DO);
//    taskEntity.setTitle(newTask.get(1));
//    taskEntity.setDescription(newTask.get(2));
//    taskEntity.setStatus(Status.TO_DO);
    taskRepository.save(taskEntity);
    log.debug("Save task {}", taskEntity);
  }

  public void updateTask(Long id, TaskEntity taskEntity) {
    TaskEntity taskFromDB = getTask(id);
    taskFromDB.setTitle(taskEntity.getTitle());
    taskFromDB.setDescription(taskEntity.getDescription());
    taskFromDB.setStatus(taskEntity.getStatus());
    taskFromDB.setDeadline(taskEntity.getDeadline());

    taskRepository.save(taskFromDB);
    log.debug("Update task {}", taskFromDB);
  }

  public void deleteTask(Long id) {
    TaskEntity taskEntity = getTask(id);
    taskRepository.delete(taskEntity);
    log.debug("Delete task {}", taskEntity);
  }

  public List<TaskEntity> getAllTasksByUserName(String userName) {
    log.debug("Try to find tasks with userName {}", userName);
    return taskRepository.findAllByUserName(userName);
  }

  public void deleteTaskByTitleAndUserName(String title, String userName) {
    TaskEntity taskEntity = getTaskByTitleAndUserName(title, userName);
    taskRepository.delete(taskEntity);
    log.debug("Delete task {} for user {}", title, userName);
  }
}
