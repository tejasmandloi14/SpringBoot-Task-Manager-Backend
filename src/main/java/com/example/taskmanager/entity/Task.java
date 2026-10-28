package com.example.taskmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter                 // creates all getters
@Setter                 // creates all setters
@NoArgsConstructor      // empty constructor (JPA needs this)
public class Task {
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.MEDIUM;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @JsonIgnore                      // never send the User object (it includes the password hash)
    private User owner;

    // Shown in the JSON as "ownerUsername"
    public String getOwnerUsername() {
        return owner != null ? owner.getUsername() : null;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.TODO;

    private LocalDateTime createdAt = LocalDateTime.now();
}