package com.sirbiladze.homeassistaint.model.entity;

import com.sirbiladze.homeassistaint.model.Priority;
import com.sirbiladze.homeassistaint.model.Status;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tasks")
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String title;

  @Column
  private String description;

  @Column
  private Priority priority;

  @Column
  private Status status;

  @Column
  private LocalDate deadline;

}
