package com.internship.tool.entity;

import jakarta.validation.constraints.*; // Validation annotations (e.g., @NotBlank)
import jakarta.persistence.*; // JPA annotations (Entity, Table, Column)
import lombok.*; // Lombok to reduce boilerplate code

import org.springframework.data.annotation.CreatedDate; // Auto set created time
import org.springframework.data.annotation.LastModifiedDate; // Auto set updated time
import org.springframework.data.jpa.domain.support.AuditingEntityListener; // Enables auditing

import io.swagger.v3.oas.annotations.media.Schema; // Swagger API documentation

import java.time.LocalDateTime;

@Entity // Marks this class as a database entity
@Table(name = "policies") // Maps to "policies" table in DB
@EntityListeners(AuditingEntityListener.class) // Enables automatic timestamp handling
@Data // Lombok: Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: Default constructor
@AllArgsConstructor // Lombok: All-args constructor
@Builder // Lombok: Builder pattern
@Schema(description = "Policy entity representing policy details") // Swagger model description
public class Policy {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    @Schema(description = "Unique ID of the policy", example = "1")
    private Long id;

    @NotBlank(message = "Title is required") // Validation: cannot be empty
    @Column(nullable = false) // DB constraint
    @Schema(description = "Title of the policy", example = "Data Security Policy")
    private String title;

    @NotBlank(message = "Description is required")
    @Column(columnDefinition = "TEXT") // Allows long text
    @Schema(
        description = "Detailed description of the policy",
        example = "This policy ensures data protection and encryption."
    )
    private String description;

    @NotBlank(message = "Category is required")
    @Column
    @Schema(description = "Category of the policy", example = "Security")
    private String category;

    @NotBlank(message = "Status is required")
    @Column
    @Schema(description = "Current status of the policy", example = "ACTIVE")
    private String status;

    @CreatedDate // Automatically sets creation time when record is inserted
    @Column(updatable = false) // Cannot be modified later
    @Schema(description = "Policy creation timestamp", example = "2026-05-05T10:30:00")
    private LocalDateTime createdAt;

    @LastModifiedDate // Automatically updates when record is modified
    @Schema(description = "Last updated timestamp", example = "2026-05-05T12:00:00")
    private LocalDateTime updatedAt;

    @Column
    @Schema(description = "Due date for policy review", example = "2026-06-01T00:00:00")
    private LocalDateTime dueDate; // Used to check overdue policies

    // ✅ IMPORTANT: AI generated report field (fixes your error)
    @Schema(
        description = "AI generated report for the policy",
        example = "This policy complies with security standards and requires periodic review."
    )
    @Column(columnDefinition = "TEXT") // Stores long AI response
    private String aiReport;

    
}