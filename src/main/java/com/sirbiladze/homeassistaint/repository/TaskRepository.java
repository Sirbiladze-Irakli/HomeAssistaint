package com.sirbiladze.homeassistaint.repository;

import com.sirbiladze.homeassistaint.model.entity.TaskEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

  Optional<TaskEntity> findById(Long id);

  List<TaskEntity> findAllByUserName(String userName);

  Optional<TaskEntity> findByTitleAndUserName(String title, String userName);

  void deleteByTitleAndUserName(String title, String userName);

}
