package org.example.stage4.service;

import org.example.stage4.dto.StudentDto;
import org.example.stage4.dto.TeacherBasicDto;
import org.example.stage4.entity.Student;

import java.util.List;
import java.util.Set;

/**
 * Service interface for student management
 * The interface includes basic operations for working with students and operations that return DTOs
 */
public interface StudentService {

    Set<TeacherBasicDto> getTeachersForStudent(Long studentId);

    StudentDto assignTeacherToStudent(Long studentId, Long teacherId, String notes);

    void removeTeacherFromStudent(Long studentId, Long teacherId);

    List<StudentDto> getStudentsWithoutTeachers();

    /**
     * Get all students as entities
     *
     * @return List of Student entities
     * @deprecated Better to use getAllStudentsAsDto()
     */
    @Deprecated
    List<Student> getAllStudents();

    /**
     * Get all students as DTOs
     *
     * @return List of StudentDto
     */
    List<StudentDto> getAllStudentsAsDto();

    /**
     * Get a student by ID as DTO
     *
     * @param id Student ID
     * @return Requested StudentDto
     */
    StudentDto getStudentDtoById(Long id);

    /**
     * Add a new student
     *
     * @param studentDto Data for the new student
     * @return Created Student entity
     */
    Student addStudent(StudentDto studentDto);

    /**
     * Add a new student and return DTO
     *
     * @param studentDto Data for the new student
     * @return Created StudentDto
     */
    StudentDto addStudentAndReturnDto(StudentDto studentDto);

    /**
     * Update an existing student
     *
     * @param studentDto Updated data
     * @param id         Student ID
     * @return Updated Student entity
      */
    Student updateStudent(StudentDto studentDto, Long id);

    /**
     * Update an existing student and return DTO
     *
     * @param studentDto Updated data
     * @param id         Student ID
     * @return Updated StudentDto
     */
    StudentDto updateStudentAndReturnDto(StudentDto studentDto, Long id);

    /**
     * Delete a student
     *
     * @param id Student ID to delete
     */
    void deleteStudent(Long id);
}