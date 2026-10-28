package com.example.taskmanager.repository;

import com.example.taskmanager.entity.Priority;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("""
           SELECT t FROM Task t
           WHERE (:ownerId  IS NULL OR t.owner.id = :ownerId)
             AND (:status   IS NULL OR t.status   = :status)
             AND (:priority IS NULL OR t.priority = :priority)
             AND (:keyword  IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
           """)
    Page<Task> search(@Param("ownerId") Long ownerId,
                      @Param("status") TaskStatus status,
                      @Param("priority") Priority priority,
                      @Param("keyword") String keyword,
                      Pageable pageable);
}