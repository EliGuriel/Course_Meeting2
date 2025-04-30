package org.example.stage4.service;

import org.example.stage4.dto.TeacherDto;

import java.util.List;

public interface TeacherService {

    List<TeacherDto> getAllTeachers();

    TeacherDto getTeacherById(Long id);

    TeacherDto createTeacher(TeacherDto teacherDto);

    TeacherDto updateTeacher(Long id, TeacherDto teacherDto);

    void deleteTeacher(Long id);

    TeacherDto assignStudentToTeacher(Long teacherId, Long studentId);

    void removeStudentFromTeacher(Long teacherId, Long studentId);

    /**
     * Find teachers by subject
     *
     * @param subject Subject name
     * @return List of TeacherDto
     */
    List<TeacherDto> getTeachersBySubject(String subject);


}