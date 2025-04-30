package org.example.transactionaldemo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateRequest {
    @NotNull(message = "Teacher ID is required")
    @Positive(message = "Teacher ID must be positive")
    private Long teacherId;
    
    @NotNull(message = "Student ID is required")
    @Positive(message = "Student ID must be positive")
    private Long studentId;
    
    @NotBlank(message = "New teacher name is required")
    private String newTeacherName;
    
    @NotBlank(message = "New student name is required")
    private String newStudentName;
    
    // Custom validation method to check if newTeacherName contains "error"
    public boolean containsErrorInTeacherName() {
        return newTeacherName != null && newTeacherName.contains("error");
    }
}