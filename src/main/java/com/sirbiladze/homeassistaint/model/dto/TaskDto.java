package com.sirbiladze.homeassistaint.model.dto;

import com.sirbiladze.homeassistaint.model.Priority;
import com.sirbiladze.homeassistaint.model.Status;
import java.time.LocalDate;
import lombok.Data;

@Data
public class TaskDto {

  private String title;
  private String description;
  private Priority priority;
  private Status status;
  private LocalDate deadline;

}
