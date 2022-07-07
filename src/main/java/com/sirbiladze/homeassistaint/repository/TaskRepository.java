package com.sirbiladze.homeassistaint.repository;

import com.sirbiladze.homeassistaint.model.entity.TaskEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

  Optional<TaskEntity> findById(Long id);

}
