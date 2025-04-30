package org.example.nplusone.dto;

import lombok.Data;

import java.util.List;

@Data
public class TeacherDto {
    private Long id;
    private String name;
    private String subject;
    private List<StudentDto> students;
}