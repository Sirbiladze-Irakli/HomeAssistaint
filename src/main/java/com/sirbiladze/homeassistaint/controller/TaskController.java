package com.sirbiladze.homeassistaint.controller;

import com.sirbiladze.homeassistaint.mapper.TaskMapper;
import com.sirbiladze.homeassistaint.model.dto.TaskDto;
import com.sirbiladze.homeassistaint.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TaskController extends BaseController {

  private final TaskService taskService;

  @PostMapping("tasks")
  public void save(@RequestBody TaskDto taskDto) {
    taskService.saveTask(TaskMapper.INSTANCE.fromDto(taskDto));
  }

  @GetMapping("tasks/{id}")
  public TaskDto get(@PathVariable Long id) {
    return TaskMapper.INSTANCE.toDto(taskService.getTask(id));
  }

  @PutMapping("tasks")
  public void update(@RequestBody TaskDto taskDto) {
  }

  @DeleteMapping("tasks/{id}")
  public void delete(@PathVariable Long id) {
    taskService.deleteTask(id);
  }

}
