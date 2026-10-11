package com.prepwise.repository;

import com.prepwise.entity.Task;
import com.prepwise.entity.TaskType;
import com.prepwise.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserAndTaskTypeAndCompletedFalseOrderByDueDateAsc(User user, TaskType taskType);

    void deleteByUser(User user);
}
