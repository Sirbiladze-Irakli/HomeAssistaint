package com.sirbiladze.homeassistaint.mapper;

import com.sirbiladze.homeassistaint.model.Status;
import com.sirbiladze.homeassistaint.model.dto.TaskDto;
import com.sirbiladze.homeassistaint.model.entity.TaskEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskMapper {

  TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

  TaskDto toDto(TaskEntity taskEntity);

  @Mapping(target = "id", ignore = true)
  TaskEntity fromDto(TaskDto taskDto);

  List<TaskDto> allToDto(List<TaskEntity> taskEntities);

//  @Mapping(source = "newTask.get(1)", target = "title")
//  @Mapping(source = "newTask.get(2)", target = "description")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "deadline", ignore = true)
  TaskEntity map(String title, String description, String chatId, String userName, Status status);

}
