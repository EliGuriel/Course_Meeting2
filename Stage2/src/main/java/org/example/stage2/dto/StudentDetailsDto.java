package org.example.stage2.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetailsDto {
    private Long id;
    
    // TODO fix entities relationship: Removed @NotNull to allow for creation of student with details in single request
    // The studentId is required only when creating or updating StudentDetails directly, 
    // but not when creating a new Student with details in a single operation
    private Long studentId;

    @Size(max = 255, message = "Address must be less than 255 characters")
    private String address;

    // regex: for Israeli phone numbers
    @Pattern(regexp = "^\\+?(972|0)([23459]\\d{7}|5\\d{8})$",
            message = "Phone number must be a valid Israeli phone number (e.g., +972541234567 or 0541234567)")
    private String phoneNumber;

    @Size(max = 100, message = "Emergency contact name must be less than 100 characters")
    private String emergencyContactName;

    // regex: for Israeli phone numbers
    @Pattern(regexp = "^\\+?(972|0)([23459]\\d{7}|5\\d{8})$",
            message = "Emergency contact phone must be a valid Israeli phone number (e.g., +972541234567 or 0541234567)")
    private String emergencyContactPhone;
}