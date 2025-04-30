package org.example.stage3.service;

import org.example.stage3.dto.StudentDto;
import org.example.stage3.dto.TeacherBasicDto;

import java.util.List;

/**
 * Service interface for student management
 */
public interface StudentService {
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
     * Add a new student and return DTO
     *
     * @param studentDto Data for the new student
     * @return Created StudentDto
     */
    StudentDto addStudentAndReturnDto(StudentDto studentDto);

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
    
    /**
     * Get the teacher for a specific student
     * 
     * @param studentId Student ID
     * @return TeacherBasicDto
     */
    TeacherBasicDto getTeacherForStudent(Long studentId);
    
    /**
     * Assign a teacher to a student
     * 
     * @param studentId Student ID
     * @param teacherId Teacher ID
     * @return Updated StudentDto
     */
    StudentDto assignTeacherToStudent(Long studentId, Long teacherId);
    
    /**
     * Remove a teacher from a student
     * 
     * @param studentId Student ID
     */
    void removeTeacherFromStudent(Long studentId);
    
    /**
     * Get students without a teacher
     * 
     * @return List of StudentDto
     */
    List<StudentDto> getStudentsWithoutTeacher();
}