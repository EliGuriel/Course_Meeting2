package org.example.nplusone.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "student_details")
@Data
public class StudentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;

    private String phoneNumber;
}