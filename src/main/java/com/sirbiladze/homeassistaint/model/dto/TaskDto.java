package com.sirbiladze.homeassistaint.model.dto;

import com.sirbiladze.homeassistaint.model.Priority;
import com.sirbiladze.homeassistaint.model.Status;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskDto {

  String chatId;
  String userName;
  String title;
  String description;
  Priority priority;
  Status status;
  LocalDate deadline;

}
