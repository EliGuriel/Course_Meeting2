package org.example.stage4.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a student in the system.
 * Has a bidirectional One-to-One relationship with StudentDetails
 * and a Many-to-Many relationship with Teacher.
 */
@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
     * One-to-One relationship with StudentDetails.
     * This side owns the relationship (has the foreign key).
     * 
     * The orphanRemoval=true ensures that when a Student is disconnected from 
     * its StudentDetails, the details are automatically deleted from the database,
     * preventing orphaned records.
     * 
     * CascadeType.ALL ensures all operations (persist, merge, remove, etc.) 
     * performed on Student are cascaded to the associated StudentDetails.
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "details_id")
    private StudentDetails details;
    
    /**
     * Many-to-Many relationship with Teacher entities.
     * This is the inverse side of the relationship as indicated by 'mappedBy'.
     * 
     * A student can have multiple teachers, and a teacher can have multiple students.
     * The relationship is managed by the Teacher entity through a join table.
     */
    @ManyToMany(mappedBy = "students")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<Teacher> teachers = new HashSet<>();
}