package org.example.stage3.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
@Data
@NoArgsConstructor
public class Teacher {

    public Teacher(Long id, String name, String subject, List<Student> students) {
        this.id = id;
        this.name = name;
        this.subject = subject;
        // we are using the setter to ensure the student's list is initialized properly
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

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Student> students = new ArrayList<>();

    // Utility methods for managing the bidirectional relationship
    public void addStudent(Student student) {
        students.add(student);
        student.setTeacher(this);
    }

    public void removeStudent(Student student) {
        students.remove(student);
        student.setTeacher(null);
    }
}