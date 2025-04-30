package org.example.stage4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A simplified DTO for Teacher entities when used in relationships
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherBasicDto {
    private Long id;
    private String name;
    private String subject;
}