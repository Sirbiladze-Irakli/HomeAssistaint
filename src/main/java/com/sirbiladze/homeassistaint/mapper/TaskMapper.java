package com.sirbiladze.homeassistaint.mapper;

import com.sirbiladze.homeassistaint.model.constants.Status;
import com.sirbiladze.homeassistaint.model.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskMapper {

  TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "deadline", ignore = true)
  TaskEntity map(String title, String description, String chatId, String userName, Status status);

}
