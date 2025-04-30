package org.example.stage4.service;

import org.example.stage4.dto.TeacherDto;
import org.example.stage4.entity.Student;
import org.example.stage4.entity.Teacher;
import org.example.stage4.exception.AlreadyExists;
import org.example.stage4.exception.NotExists;
import org.example.stage4.mapper.TeacherMapper;
import org.example.stage4.repository.StudentRepository;
import org.example.stage4.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
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

        if (teacherDto.getStudentIds() != null && !teacherDto.getStudentIds().isEmpty()) {
            Set<Student> students = teacherDto.getStudentIds().stream()
                    .map(studentId -> studentRepository.findById(studentId)
                            .orElseThrow(() -> new NotExists("Student with id " + studentId + " not found")))
                    .collect(Collectors.toSet());
            teacher.setStudents(students);
            
            // Update the bidirectional relationship
            for (Student student : students) {
                student.getTeachers().add(teacher);
            }
        }

        Teacher savedTeacher = teacherRepository.save(teacher);
        return teacherMapper.toDto(savedTeacher);
    }

    @Override
    @Transactional
    public TeacherDto updateTeacher(Long id, TeacherDto teacherDto) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new NotExists("Teacher with id " + id + " does not exist"));

        teacherMapper.updateEntityFromDto(teacher, teacherDto);

        if (teacherDto.getStudentIds() != null) {
            // Get current students
            Set<Student> currentStudents = teacher.getStudents();
            
            // Get new students from IDs
            Set<Student> newStudents = teacherDto.getStudentIds().stream()
                    .map(studentId -> studentRepository.findById(studentId)
                            .orElseThrow(() -> new NotExists("Student with id " + studentId + " not found")))
                    .collect(Collectors.toSet());
            
            // Remove teacher from students that are no longer associated
            for (Student student : currentStudents) {
                if (!newStudents.contains(student)) {
                    student.getTeachers().remove(teacher);
                }
            }
            
            // Add teacher to new students
            for (Student student : newStudents) {
                if (!currentStudents.contains(student)) {
                    student.getTeachers().add(teacher);
                }
            }
            
            // Update the teacher's students
            teacher.setStudents(newStudents);
        }

        Teacher updatedTeacher = teacherRepository.save(teacher);
        return teacherMapper.toDto(updatedTeacher);
    }

    @Override
    @Transactional
    public void deleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new NotExists("Teacher with id " + id + " does not exist"));
        
        // Remove the teacher from all associated students
        for (Student student : teacher.getStudents()) {
            student.getTeachers().remove(teacher);
        }
        
        teacherRepository.delete(teacher);
    }

    @Override
    @Transactional
    public TeacherDto assignStudentToTeacher(Long teacherId, Long studentId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new NotExists("Teacher with id " + teacherId + " does not exist"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotExists("Student with id " + studentId + " does not exist"));

        // Check if already assigned
        if (teacher.getStudents().contains(student)) {
            throw new AlreadyExists("Student is already assigned to this teacher");
        }

        // Update the bidirectional relationship
        teacher.getStudents().add(student);
        student.getTeachers().add(teacher);
        
        Teacher savedTeacher = teacherRepository.save(teacher);
        return teacherMapper.toDto(savedTeacher);
    }

    @Override
    @Transactional
    public void removeStudentFromTeacher(Long teacherId, Long studentId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new NotExists("Teacher with id " + teacherId + " does not exist"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotExists("Student with id " + studentId + " does not exist"));

        // Check if the assignment exists
        if (!teacher.getStudents().contains(student)) {
            throw new NotExists("Student is not assigned to this teacher");
        }

        // Update the bidirectional relationship
        teacher.getStudents().remove(student);
        student.getTeachers().remove(teacher);
        
        teacherRepository.save(teacher);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherDto> getTeachersBySubject(String subject) {
        List<Teacher> teachers = teacherRepository.findBySubject(subject);
        return teachers.stream()
                .map(teacherMapper::toDto)
                .collect(Collectors.toList());
    }
}