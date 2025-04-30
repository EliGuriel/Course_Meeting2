package org.example.stage3.service;

import org.example.stage3.dto.TeacherDto;
import org.example.stage3.entity.Student;
import org.example.stage3.entity.Teacher;
import org.example.stage3.exception.AlreadyExists;
import org.example.stage3.exception.NotExists;
import org.example.stage3.mapper.TeacherMapper;
import org.example.stage3.repository.StudentRepository;
import org.example.stage3.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final TeacherMapper teacherMapper;

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository,
                             StudentRepository studentRepository,
                             TeacherMapper teacherMapper) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.teacherMapper = teacherMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherDto> getAllTeachers() {
        return teacherRepository.findAll().stream()
                .map(teacherMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherDto getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findByIdWithStudents(id)
                .orElseThrow(() -> new NotExists("Teacher with id " + id + " does not exist"));
        return teacherMapper.toDto(teacher);
    }

    @Override
    @Transactional
    public TeacherDto createTeacher(TeacherDto teacherDto) {
        Teacher teacher = teacherMapper.toEntity(teacherDto);
        
        // If student IDs are provided, associate them with the teacher
        if (teacherDto.getStudentIds() != null && !teacherDto.getStudentIds().isEmpty()) {
            for (Long studentId : teacherDto.getStudentIds()) {
                Student student = studentRepository.findById(studentId)
                        .orElseThrow(() -> new NotExists("Student with id " + studentId + " not found"));
                
                teacher.addStudent(student);
            }
        }
        
        Teacher savedTeacher = teacherRepository.save(teacher);
        return teacherMapper.toDto(savedTeacher);
    }

    @Override
    @Transactional
    public TeacherDto updateTeacher(Long id, TeacherDto teacherDto) {
        Teacher teacher = teacherRepository.findByIdWithStudents(id)
                .orElseThrow(() -> new NotExists("Teacher with id " + id + " does not exist"));
        
        teacherMapper.updateEntityFromDto(teacher, teacherDto);
        
        // Handle student associations
        if (teacherDto.getStudentIds() != null) {
            // First remove all current students
            List<Student> currentStudents = teacher.getStudents();
            for (Student student : new ArrayList<>(currentStudents)) {
                teacher.removeStudent(student);
            }
            
            // Then add the students from the DTO
            for (Long studentId : teacherDto.getStudentIds()) {
                Student student = studentRepository.findById(studentId)
                        .orElseThrow(() -> new NotExists("Student with id " + studentId + " not found"));
                
                teacher.addStudent(student);
            }
        }
        
        Teacher updatedTeacher = teacherRepository.save(teacher);
        return teacherMapper.toDto(updatedTeacher);
    }

    @Override
    @Transactional
    public void deleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findByIdWithStudents(id)
                .orElseThrow(() -> new NotExists("Teacher with id " + id + " does not exist"));
        
        // Remove teacher association from all students
        for (Student student : new ArrayList<>(teacher.getStudents())) {
            teacher.removeStudent(student);
        }
        
        teacherRepository.delete(teacher);
    }

    @Override
    @Transactional
    public TeacherDto assignStudentToTeacher(Long teacherId, Long studentId) {
        Teacher teacher = teacherRepository.findByIdWithStudents(teacherId)
                .orElseThrow(() -> new NotExists("Teacher with id " + teacherId + " does not exist"));
        
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotExists("Student with id " + studentId + " does not exist"));
        
        // Check if student is already assigned to this teacher
        if (teacher.getStudents().contains(student)) {
            throw new AlreadyExists("Student with id " + studentId + " is already assigned to teacher with id " + teacherId);
        }
        
        // If student has another teacher, remove that association
        if (student.getTeacher() != null) {
            student.getTeacher().removeStudent(student);
        }
        
        // Add student to this teacher
        teacher.addStudent(student);
        
        Teacher savedTeacher = teacherRepository.save(teacher);
        return teacherMapper.toDto(savedTeacher);
    }

    @Override
    @Transactional
    public void removeStudentFromTeacher(Long teacherId, Long studentId) {
        Teacher teacher = teacherRepository.findByIdWithStudents(teacherId)
                .orElseThrow(() -> new NotExists("Teacher with id " + teacherId + " does not exist"));
        
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotExists("Student with id " + studentId + " does not exist"));
        
        // Check if student is assigned to this teacher
        if (!teacher.getStudents().contains(student)) {
            throw new NotExists("Student with id " + studentId + " is not assigned to teacher with id " + teacherId);
        }
        
        // Remove student from this teacher
        teacher.removeStudent(student);
        
        teacherRepository.save(teacher);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherDto> getTeachersBySubject(String subject) {
        return teacherRepository.findBySubject(subject).stream()
                .map(teacherMapper::toDto)
                .collect(Collectors.toList());
    }
}