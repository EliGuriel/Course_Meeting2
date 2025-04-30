package org.example.transactionaldemo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateRequest {
    @NotBlank(message = "Teacher name is required")
    private String teacherName;
    
    @NotNull(message = "Subject is required")
    private String subject;
    
    @NotBlank(message = "Student name is required")
    private String studentName;
    
    @NotBlank(message = "Student email is required")
    @Email(message = "Invalid email format")
    private String studentEmail;
}