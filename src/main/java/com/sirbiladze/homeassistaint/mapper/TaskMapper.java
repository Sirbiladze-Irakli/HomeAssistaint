package com.sirbiladze.homeassistaint.mapper;

import com.sirbiladze.homeassistaint.model.dto.TaskDto;
import com.sirbiladze.homeassistaint.model.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskMapper {

  TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

  TaskDto toDto(Task task);
  Task fromDto(TaskDto taskDto);

}
