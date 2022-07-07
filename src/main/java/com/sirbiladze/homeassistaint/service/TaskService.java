package com.sirbiladze.homeassistaint.service;

import static java.lang.String.format;

import com.sirbiladze.homeassistaint.model.entity.TaskEntity;
import com.sirbiladze.homeassistaint.model.exception.NotFoundException;
import com.sirbiladze.homeassistaint.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

  private final TaskRepository taskRepository;

  public TaskEntity getTask(Long id) {
    log.warn("Try to find task with id {}", id);
    return taskRepository.findById(id).orElseThrow(
        () -> new NotFoundException(format("Not found Task with id %d", id)));
  }

  public void saveTask(TaskEntity taskEntity) {
    taskRepository.save(taskEntity);
    log.warn("Save task {}", taskEntity);
  }

  public void deleteTask(Long id) {
    TaskEntity taskEntity = getTask(id);
    taskRepository.delete(taskEntity);
    log.warn("Delete task {}", taskEntity);
  }

}
