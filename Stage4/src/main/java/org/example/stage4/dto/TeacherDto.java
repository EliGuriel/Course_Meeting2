package org.example.stage4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDto {
    private Long id;
    private String name;
    private String subject;
    private Set<Long> studentIds; // רק IDs של תלמידים
}