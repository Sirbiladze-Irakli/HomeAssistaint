package com.sirbiladze.homeassistaint.mapper;

import com.sirbiladze.homeassistaint.model.dto.TaskDto;
import com.sirbiladze.homeassistaint.model.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskMapper {

  TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

  TaskDto toDto(TaskEntity taskEntity);

  @Mapping(target = "id", ignore = true)
  TaskEntity fromDto(TaskDto taskDto);

}
