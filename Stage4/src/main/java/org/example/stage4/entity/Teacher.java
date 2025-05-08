package org.example.stage4.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a teacher in the system.
 * Has a Many-to-Many relationship with a Student.
 */
@Entity
@Table(name = "teachers")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private Long id;

    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Size(min = 2, max = 50, message = "Subject must be between 2 and 50 characters")
    @Column(name = "subject", nullable = false, length = 50)
    private String subject;

    /**
     * Many-to-Many relationship with Student entities.
     * This is the owning side of the relationship, defined by the @JoinTable annotation.
     * 
     * The relationship is managed through a join table named "teacher_student"
     * with columns "teacher_id" and "student_id" linking both entities.
     * 
     * Using @ToString.Exclude to prevent infinite recursion in toString() method.
     */

    @ManyToMany
    @JoinTable(
            name = "teacher_student",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @ToString.Exclude
    @JsonIgnore  // Prevents infinite recursion during JSON serialization
    @EqualsAndHashCode.Exclude
    private Set<Student> students = new HashSet<>();  // Initialize to avoid null pointer

    /**
     * Utility method to add a student to this teacher.
     * Manages both sides of the relationship.
     *
     * @param student The student to add
     */
    public void addStudent(Student student) {
        this.students.add(student);
        student.getTeachers().add(this);
    }

    /**
     * Utility method to remove a student from this teacher.
     * Manages both sides of the relationship.
     *
     * @param student The student to remove
     */
    public void removeStudent(Student student) {
        this.students.remove(student);
        student.getTeachers().remove(this);
    }
}