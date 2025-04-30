package org.example.stage3.service;

import org.example.stage3.dto.StudentDto;
import org.example.stage3.dto.TeacherBasicDto;
import org.example.stage3.entity.Student;
import org.example.stage3.entity.Teacher;
import org.example.stage3.exception.AlreadyExists;
import org.example.stage3.exception.NotExists;
import org.example.stage3.exception.StudentIdAndIdMismatch;
import org.example.stage3.mapper.StudentMapper;
import org.example.stage3.mapper.TeacherMapper;
import org.example.stage3.repository.StudentRepository;
import org.example.stage3.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final StudentMapper studentMapper;
    private final TeacherMapper teacherMapper;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository,
                             TeacherRepository teacherRepository,
                             StudentMapper studentMapper,
                             TeacherMapper teacherMapper) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.studentMapper = studentMapper;
        this.teacherMapper = teacherMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> getAllStudentsAsDto() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDto getStudentDtoById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NotExists("Student with id " + id + " does not exist"));

        return studentMapper.toDto(student);
    }

    @Override
    @Transactional
    public StudentDto addStudentAndReturnDto(StudentDto studentDto) {
        // Check if a student with the same email already exists
        if (studentRepository.findByEmail(studentDto.getEmail()).isPresent()) {
            throw new AlreadyExists("Student with email " + studentDto.getEmail() + " already exists");
        }

        // Convert DTO to entity using the mapper
        Student student = studentMapper.toEntity(studentDto);
        
        // Handle teacher assignment if provided
        if (studentDto.getTeacher() != null && studentDto.getTeacher().getId() != null) {
            Teacher teacher = teacherRepository.findById(studentDto.getTeacher().getId())
                    .orElseThrow(() -> new NotExists("Teacher with id " + studentDto.getTeacher().getId() + " does not exist"));
            
            teacher.addStudent(student);
        }
        
        Student added = studentRepository.save(student);
        return studentMapper.toDto(added);
    }

    @Override
    @Transactional
    public StudentDto updateStudentAndReturnDto(StudentDto studentDto, Long id) {
        // Check if the ID parameter matches the student's ID (if DTO has ID)
        if (studentDto.getId() != null && !studentDto.getId().equals(id)) {
            throw new StudentIdAndIdMismatch("Path ID " + id + " does not match body ID " + studentDto.getId());
        }

        // Check if a student exists
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new NotExists("Student with id " + id + " does not exist"));

        // Check if another student already uses the updated email
        studentRepository.findByEmail(studentDto.getEmail())
                .ifPresent(student -> {
                    if (!student.getId().equals(id)) {
                        throw new AlreadyExists("Email " + studentDto.getEmail() + " is already in use");
                    }
                });

        // Update the existing entity from the DTO
        studentMapper.updateEntityFromDto(existingStudent, studentDto);
        
        // Handle teacher assignment if provided
        if (studentDto.getTeacher() != null && studentDto.getTeacher().getId() != null) {
            // If student already has a teacher and it's different, remove from old teacher
            if (existingStudent.getTeacher() != null && 
                !existingStudent.getTeacher().getId().equals(studentDto.getTeacher().getId())) {
                existingStudent.getTeacher().removeStudent(existingStudent);
            }
            
            // Assign to new teacher
            Teacher teacher = teacherRepository.findById(studentDto.getTeacher().getId())
                    .orElseThrow(() -> new NotExists("Teacher with id " + studentDto.getTeacher().getId() + " does not exist"));
            
            teacher.addStudent(existingStudent);
        } else if (studentDto.getTeacher() == null && existingStudent.getTeacher() != null) {
            // If teacher is null in DTO but student has a teacher, remove from teacher
            existingStudent.getTeacher().removeStudent(existingStudent);
        }

        Student updated = studentRepository.save(existingStudent);
        return studentMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        // Check if student exists
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NotExists("Student with id " + id + " does not exist"));
        
        // If student has a teacher, remove from teacher
        if (student.getTeacher() != null) {
            student.getTeacher().removeStudent(student);
        }

        studentRepository.delete(student);
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherBasicDto getTeacherForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotExists("Student with id " + studentId + " does not exist"));
        
        if (student.getTeacher() == null) {
            throw new NotExists("Student with id " + studentId + " does not have a teacher");
        }
        
        return teacherMapper.toBasicDto(student.getTeacher());
    }

    @Override
    @Transactional
    public StudentDto assignTeacherToStudent(Long studentId, Long teacherId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotExists("Student with id " + studentId + " does not exist"));
        
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new NotExists("Teacher with id " + teacherId + " does not exist"));
        
        // If student already has a teacher, remove from old teacher
        if (student.getTeacher() != null) {
            if (student.getTeacher().getId().equals(teacherId)) {
                throw new AlreadyExists("Student is already assigned to this teacher");
            }
            student.getTeacher().removeStudent(student);
        }
        
        // Assign to new teacher
        teacher.addStudent(student);
        
        Student updated = studentRepository.save(student);
        return studentMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void removeTeacherFromStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotExists("Student with id " + studentId + " does not exist"));
        
        if (student.getTeacher() == null) {
            throw new NotExists("Student with id " + studentId + " does not have a teacher");
        }
        
        student.getTeacher().removeStudent(student);
        studentRepository.save(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> getStudentsWithoutTeacher() {
        List<Student> studentsWithoutTeacher = studentRepository.findAll().stream()
                .filter(student -> student.getTeacher() == null)
                .collect(Collectors.toList());
        
        return studentsWithoutTeacher.stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }
}