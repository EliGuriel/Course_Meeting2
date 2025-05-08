package org.example.stage3.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a teacher who can have multiple students.
 * Demonstrates a One-to-Many relationship with Student entity.
 */
@Entity
@Table(name = "teachers")
@Data
@NoArgsConstructor

public class Teacher {

    /**
     * Constructor with parameters for all fields except the automatically generated ID.
     * Initializes the students list to ensure it's never null.
     */
    public Teacher(Long id, String name, String subject, List<Student> students) {
        this.id = id;
        this.name = name;
        this.subject = subject;
        // Using the setter to ensure the student's list is initialized properly
        this.students = students != null ? students : new ArrayList<>();
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @NotBlank(message = "Subject is required")
    @Size(min = 2, max = 50, message = "Subject must be between 2 and 50 characters")
    @Column(name = "subject", nullable = false, length = 50)
    private String subject;

    /**
     * One-to-Many relationship with Student entities.
     * This is the inverse (non-owning) side of the relationship 
     * as indicated by the 'mappedBy' attribute.
     * 
     * @ToString.Exclude - Prevents infinite recursion in toString() method
     * @JsonIgnore - Prevents infinite recursion during JSON serialization
     *              (Teacher → Students → Teacher → ...)
     * 
     * Using both annotations is appropriate on this side of the relationship
     * because this is the collection/inverse side.
     */
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private List<Student> students = new ArrayList<>();

    /**
     * Utility method for managing the bidirectional relationship.
     * Adds a student to this teacher and sets the teacher reference in the student.
     * 
     * @param student The student to add to this teacher
     */
    public void addStudent(Student student) {
        students.add(student);
        student.setTeacher(this);
    }

    /**
     * Utility method for managing the bidirectional relationship.
     * Removes a student from this teacher and clears the teacher reference in the student.
     * 
     * @param student The student to remove from this teacher
     */
    public void removeStudent(Student student) {
        students.remove(student);
        student.setTeacher(null);
    }
}