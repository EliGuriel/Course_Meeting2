package org.example.stage2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entity representing a student in the system.
 * Has a bidirectional One-to-One relationship with StudentDetails.
 */
@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Min(value = 0, message = "Age must be a positive number")
    @Column(nullable = false)
    private double age;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    /**
     * This relationship uses both cascade and orphanRemoval mechanisms:
     *
     * 1. cascade = CascadeType.ALL:
     *    - Propagates all persistence operations (save, update, delete, etc.)
     *      from the Student entity to its associated StudentDetails entity
     *    - When we persist, update, or remove a Student, the same operation
     *      automatically applies to its linked StudentDetails
     *
     * 2. orphanRemoval = true:
     *    - Handles the specific case when a Student is disconnected from its StudentDetails
     *      (e.g., when calling student.setDetails(null))
     *    - Ensures the orphaned StudentDetails entity is automatically deleted from the database
     *    - Prevents orphaned records that would otherwise remain in the database
     *
     * Using both mechanisms provides comprehensive lifecycle management of the dependent
     * StudentDetails entity in all scenarios.
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "details_id")
    private StudentDetails details;
}