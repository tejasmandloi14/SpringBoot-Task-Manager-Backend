package com.example.taskmanager.dto;

import com.example.taskmanager.entity.Priority;
import com.example.taskmanager.entity.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record TaskRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title must be under 100 characters")
        String title,

        String description,

        TaskStatus status,

        @FutureOrPresent(message = "Due date cannot be in the past")
        LocalDate dueDate,

        Priority priority
) {}