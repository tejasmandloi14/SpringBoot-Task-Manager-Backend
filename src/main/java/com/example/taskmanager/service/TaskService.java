package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.entity.Priority;
import com.example.taskmanager.entity.Role;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.TaskStatus;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repo;
    private final UserRepository userRepo;

    // ---------- helpers ----------

    private User currentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private boolean isAdmin(User user) {
        return user.getRole() == Role.ADMIN;
    }

    private void applyRequest(Task task, TaskRequest req) {
        task.setTitle(req.title());
        task.setDescription(req.description());
        task.setDueDate(req.dueDate());
        if (req.status() != null)   task.setStatus(req.status());
        if (req.priority() != null) task.setPriority(req.priority());
    }

    // ---------- operations ----------

    public Page<Task> getAll(TaskStatus status, Priority priority, String keyword, Pageable pageable) {
        User me = currentUser();
        Long ownerId = isAdmin(me) ? null : me.getId();
        String search = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        return repo.search(ownerId, status, priority, search, pageable);
    }

    public Task getById(Long id) {
        Task task = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));

        User me = currentUser();
        boolean isOwner = task.getOwner() != null && task.getOwner().getId().equals(me.getId());

        if (!isAdmin(me) && !isOwner) {
            throw new ResourceNotFoundException("Task not found with id " + id);
        }
        return task;
    }

    public Task create(TaskRequest req) {
        Task task = new Task();
        applyRequest(task, req);
        task.setOwner(currentUser());
        return repo.save(task);
    }

    public Task update(Long id, TaskRequest req) {
        Task task = getById(id);
        applyRequest(task, req);
        return repo.save(task);
    }

    public void delete(Long id) {
        repo.delete(getById(id));
    }
}