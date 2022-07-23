package com.sirbiladze.homeassistaint.service;

import static java.lang.String.format;

import com.sirbiladze.homeassistaint.model.constants.Status;
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

  public void updateTask(Long id, TaskEntity taskEntity) {
    TaskEntity taskFromDB = getTask(id);
    taskFromDB.setTitle(taskEntity.getTitle());
    taskFromDB.setDescription(taskEntity.getDescription());
    taskFromDB.setStatus(taskEntity.getStatus());
    taskFromDB.setDeadline(taskEntity.getDeadline());

    taskRepository.save(taskFromDB);
    log.debug("Update task {}", taskFromDB);
  }

  public void updateTaskStatus(String title, String userName, Status status) {
    TaskEntity taskEntity = getTaskByTitleAndUserName(title, userName);
    taskEntity.setStatus(status);
    log.debug("Update status for user \"{}\" task \"{}\" ", userName, title);
    saveTask(taskEntity);
  }

  public void updateTaskDescription(String title, String userName, String description) {
    TaskEntity taskEntity = getTaskByTitleAndUserName(title, userName);
    taskEntity.setDescription(description);
    log.debug("Update description for user \"{}\" task \"{}\" ", userName, title);
    saveTask(taskEntity);
  }

  public void updateTaskTitle(String title, String newTitle, String userName) {
    TaskEntity taskEntity = getTaskByTitleAndUserName(title, userName);
    taskEntity.setTitle(newTitle);
    log.debug("Update title for user \"{}\" task \"{}\" to \"{}\" ", userName, title, newTitle);
    saveTask(taskEntity);
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
