package org.example.stage4.mapper;

import org.example.stage4.dto.TeacherBasicDto;
import org.example.stage4.dto.TeacherDto;
import org.example.stage4.entity.Student;
import org.example.stage4.entity.Teacher;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for Teacher entity and DTOs
 */
@Component
public class TeacherMapper {

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

        return teacher;
    }

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

        // Map student IDs if present
        if (entity.getStudents() != null) {
            Set<Long> studentIds = entity.getStudents().stream()
                    .map(Student::getId)
                    .collect(Collectors.toSet());
            dto.setStudentIds(studentIds);
        } else {
            dto.setStudentIds(new HashSet<>());
        }

        return dto;
    }

    /**
     * Convert Teacher entity to TeacherBasicDto
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
     * Update existing Teacher entity from DTO
     */
    public void updateEntityFromDto(Teacher entity, TeacherDto dto) {
        if (entity == null || dto == null) {
            return;
        }

        entity.setName(dto.getName());
        entity.setSubject(dto.getSubject());
    }
}