package org.example.stage4.service;

import org.example.stage4.dto.StudentDto;
import org.example.stage4.dto.TeacherBasicDto;
import org.example.stage4.entity.Student;
import org.example.stage4.entity.Teacher;
import org.example.stage4.exception.AlreadyExists;
import org.example.stage4.exception.NotExists;
import org.example.stage4.exception.StudentIdAndIdMismatch;
import org.example.stage4.mapper.StudentMapper;
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
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final TeacherRepository teacherRepository;
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
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
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
    public Student addStudent(StudentDto studentDto) {
        // Check if a student with the same email already exists
        if (studentRepository.findByEmail(studentDto.getEmail()).isPresent()) {
            throw new AlreadyExists("Student with email " + studentDto.getEmail() + " already exists");
        }

        // Convert DTO to entity using the mapper
        Student student = studentMapper.toEntity(studentDto);
        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public StudentDto addStudentAndReturnDto(StudentDto studentDto) {
        Student added = addStudent(studentDto);
        return studentMapper.toDto(added);
    }

    @Override
    @Transactional
    public Student updateStudent(StudentDto studentDto, Long id) {
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

        return studentRepository.save(existingStudent);
    }

    @Override
    @Transactional
    public StudentDto updateStudentAndReturnDto(StudentDto studentDto, Long id) {
        Student updated = updateStudent(studentDto, id);
        return studentMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        // Check if student exists
        if (!studentRepository.existsById(id)) {
            throw new NotExists("Student with id " + id + " does not exist");
        }

        studentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<TeacherBasicDto> getTeachersForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotExists("Student with id " + studentId + " does not exist"));

        return student.getTeachers().stream()
                .map(teacherMapper::toBasicDto)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public StudentDto assignTeacherToStudent(Long studentId, Long teacherId, String notes) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotExists("Student with id " + studentId + " does not exist"));

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new NotExists("Teacher with id " + teacherId + " does not exist"));

        // Check if already assigned
        if (student.getTeachers().contains(teacher)) {
            throw new AlreadyExists("Teacher is already assigned to this student");
        }

        // Assign teacher to student
        student.getTeachers().add(teacher);
        teacher.getStudents().add(student);
        
        studentRepository.save(student);

        return studentMapper.toDto(student);
    }

    @Override
    @Transactional
    public void removeTeacherFromStudent(Long studentId, Long teacherId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotExists("Student with id " + studentId + " does not exist"));

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new NotExists("Teacher with id " + teacherId + " does not exist"));

        // Check if assignment exists
        if (!student.getTeachers().contains(teacher)) {
            throw new NotExists("Teacher is not assigned to this student");
        }

        // Remove the relationship
        student.getTeachers().remove(teacher);
        teacher.getStudents().remove(student);
        
        studentRepository.save(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> getStudentsWithoutTeachers() {
        List<Student> students = studentRepository.findStudentsWithoutTeachers();
        return students.stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }
}