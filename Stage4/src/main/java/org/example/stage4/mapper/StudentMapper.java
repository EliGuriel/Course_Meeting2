package org.example.stage4.mapper;

import org.example.stage4.dto.StudentDetailsDto;
import org.example.stage4.dto.StudentDto;
import org.example.stage4.entity.Student;
import org.example.stage4.entity.StudentDetails;
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

        // map Student details DTO to StudentDetails
        if (dto.getDetails() != null) {
            StudentDetails details = toEntity(dto.getDetails());
            details.setStudent(student);
            student.setDetails(details);
        }

        return student;
    }


    /**
     * map StudentDetailsDto to StudentDetails
     * @param dto for conversion
     * @return new StudentDetails entity
     */
    public StudentDetails toEntity(StudentDetailsDto dto) {
        if (dto == null) {
            return null;
        }

        StudentDetails details = new StudentDetails();
        details.setId(dto.getId());
        details.setAddress(dto.getAddress());
        details.setPhoneNumber(dto.getPhoneNumber());
        details.setEmergencyContactName(dto.getEmergencyContactName());
        details.setEmergencyContactPhone(dto.getEmergencyContactPhone());

        return details;
    }

    /**
     * StudentDto to Student
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

        // map StudentDetails to StudentDetailsDto if exists
        if (entity.getDetails() != null) {
            StudentDetailsDto detailsDto = toDto(entity.getDetails());
            
            // TODO fix entities relationship: Set the studentId in the details when converting from entity to DTO
            // This ensures the studentId is always set in nested DTOs, even during creation
            if (entity.getId() != null) {
                detailsDto.setStudentId(entity.getId());
            }
            
            dto.setDetails(detailsDto);
        }

        return dto;
    }

    /**
     * map StudentDetails to StudentDetailsDto
     *
     * @param entity StudentDetails entity for conversion
     * @return StudentDetailsDto
     */
    public StudentDetailsDto toDto(StudentDetails entity) {
        if (entity == null) {
            return null;
        }

        StudentDetailsDto dto = new StudentDetailsDto();
        dto.setId(entity.getId());
        dto.setAddress(entity.getAddress());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setEmergencyContactName(entity.getEmergencyContactName());
        dto.setEmergencyContactPhone(entity.getEmergencyContactPhone());
        
        // TODO fix entities relationship: If the student is available, set its ID in the DTO
        // This ensures we always maintain the relationship when converting from entity to DTO
        if (entity.getStudent() != null && entity.getStudent().getId() != null) {
            dto.setStudentId(entity.getStudent().getId());
        }

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

        // update student details if they exist
        if (dto.getDetails() != null) {
            if (entity.getDetails() == null) {
                // create new details if they don't exist
                StudentDetails details = toEntity(dto.getDetails());
                details.setStudent(entity);
                entity.setDetails(details);
            } else {
                // update existing details
                updateEntityFromDto(entity.getDetails(), dto.getDetails());
            }
        }
    }

    /**
     * Updates an existing StudentDetails entity from a StudentDetailsDto
     *
     * @param entity the entity to update
     * @param dto the DTO with the new data, if null, no update will be performed
     */
    public void updateEntityFromDto(StudentDetails entity, StudentDetailsDto dto) {
        if (entity == null || dto == null) {
            return;
        }

        entity.setAddress(dto.getAddress());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setEmergencyContactName(dto.getEmergencyContactName());
        entity.setEmergencyContactPhone(dto.getEmergencyContactPhone());
    }
}