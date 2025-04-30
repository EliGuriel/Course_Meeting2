package org.example.nplusone.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
@Data
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String subject;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    private List<Student> students = new ArrayList<>();
}