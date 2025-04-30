package org.example.stage3.service;

import org.example.stage3.dto.TeacherDto;

import java.util.List;

public interface TeacherService {

    /**
     * Get all teachers
     * 
     * @return List of TeacherDto
     */
    List<TeacherDto> getAllTeachers();

    /**
     * Get teacher by ID
     * 
     * @param id Teacher ID
     * @return TeacherDto
     */
    TeacherDto getTeacherById(Long id);

    /**
     * Create a new teacher
     * 
     * @param teacherDto Teacher data
     * @return Created TeacherDto
     */
    TeacherDto createTeacher(TeacherDto teacherDto);

    /**
     * Update an existing teacher
     * 
     * @param id Teacher ID
     * @param teacherDto Updated teacher data
     * @return Updated TeacherDto
     */
    TeacherDto updateTeacher(Long id, TeacherDto teacherDto);

    /**
     * Delete a teacher
     * 
     * @param id Teacher ID
     */
    void deleteTeacher(Long id);

    /**
     * Assign a student to a teacher
     * 
     * @param teacherId Teacher ID
     * @param studentId Student ID
     * @return Updated TeacherDto
     */
    TeacherDto assignStudentToTeacher(Long teacherId, Long studentId);

    /**
     * Remove a student from a teacher
     * 
     * @param teacherId Teacher ID
     * @param studentId Student ID
     */
    void removeStudentFromTeacher(Long teacherId, Long studentId);

    /**
     * Find teachers by subject
     * 
     * @param subject Subject name
     * @return List of TeacherDto
     */
    List<TeacherDto> getTeachersBySubject(String subject);
}