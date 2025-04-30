package org.example.stage3.mapper;

import org.example.stage3.dto.TeacherBasicDto;
import org.example.stage3.dto.TeacherDto;
import org.example.stage3.entity.Student;
import org.example.stage3.entity.Teacher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TeacherMapper {

    /**
     * Convert Teacher entity to TeacherDto
     */
    public TeacherDto toDto(Teacher entity) {
        if (entity == null) {
            return null;
        }

        TeacherDto dto = new TeacherDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSubject(entity.getSubject());
        
        // Extract student IDs if students are present
        if (entity.getStudents() != null && !entity.getStudents().isEmpty()) {
            List<Long> studentIds = entity.getStudents().stream()
                    .map(Student::getId)
                    .collect(Collectors.toList());
            dto.setStudentIds(studentIds);
        }

        return dto;
    }
    
    /**
     * Convert Teacher entity to TeacherBasicDto (simplified version without students)
     */
    public TeacherBasicDto toBasicDto(Teacher entity) {
        if (entity == null) {
            return null;
        }
        
        return new TeacherBasicDto(
                entity.getId(),
                entity.getName(),
                entity.getSubject()
        );
    }

    /**
     * Convert TeacherDto to Teacher entity
     */
    public Teacher toEntity(TeacherDto dto) {
        if (dto == null) {
            return null;
        }

        Teacher teacher = new Teacher();
        teacher.setId(dto.getId());
        teacher.setName(dto.getName());
        teacher.setSubject(dto.getSubject());
        
        // Note: We don't set students here as they need to be loaded from the repository
        // That will be handled in the service layer

        return teacher;
    }

    /**
     * Update existing Teacher entity from TeacherDto
     */
    public void updateEntityFromDto(Teacher entity, TeacherDto dto) {
        if (entity == null || dto == null) {
            return;
        }

        entity.setName(dto.getName());
        entity.setSubject(dto.getSubject());
        
        // Note: We don't update students here as they need to be loaded from the repository
        // That will be handled in the service layer
    }
}