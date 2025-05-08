package org.example.stage3.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor

@ToString(exclude = {"details", "teacher"})
@EqualsAndHashCode(exclude = {"details", "teacher"})

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
     * Many-to-One relationship with Teacher.
     * This side owns the relationship (has the foreign key).
     * 
     * @ToString.Exclude prevents infinite recursion in toString() method
     * (Student → Teacher → Students → Teacher → ...)
     * 
     * We don't use @JsonIgnore here because in the JSON representation,
     * it's logical for a Student to include information about their Teacher.
     */
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    @ToString.Exclude
    private Teacher teacher;
}