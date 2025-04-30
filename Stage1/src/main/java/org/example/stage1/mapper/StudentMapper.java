package org.example.stage1.mapper;

import org.example.stage1.dto.StudentDto;
import org.example.stage1.entity.Student;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for mapping between DTO objects and entities
 */
@Component
public class StudentMapper {

    /**
     * map StudentDto to Student
     *
     * @param dto for conversion
     * @return new Student entity
     */
    public Student toEntity(StudentDto dto) {
        if (dto == null) {
            return null;
        }

        Student student = new Student();
        student.setId(dto.getId());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setAge(dto.getAge());
        student.setEmail(dto.getEmail());

        return student;
    }

    /**
     * map Student to StudentDto
     * 
     * @param entity entity for conversion
     * @return new StudentDto
     */
    public StudentDto toDto(Student entity) {
        if (entity == null) {
            return null;
        }

        StudentDto dto = new StudentDto();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setAge(entity.getAge());
        dto.setEmail(entity.getEmail());

        return dto;
    }

    /**
     * update the existing Student entity with the data from the DTO
     *
     * @param entity the entity to update
     * @param dto the DTO with the new data, if null, no update will be performed
     */
    public void updateEntityFromDto(Student entity, StudentDto dto) {
        if (entity == null || dto == null) {
            return;
        }

        // update basic fields
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setAge(dto.getAge());
        entity.setEmail(dto.getEmail());
    }
}