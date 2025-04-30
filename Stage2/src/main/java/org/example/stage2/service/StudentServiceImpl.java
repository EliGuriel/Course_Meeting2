package org.example.stage2.service;

import org.example.stage2.dto.StudentDto;
import org.example.stage2.entity.Student;
import org.example.stage2.entity.StudentDetails;
import org.example.stage2.exception.AlreadyExists;
import org.example.stage2.exception.NotExists;
import org.example.stage2.exception.StudentIdAndIdMismatch;
import org.example.stage2.mapper.StudentMapper;
import org.example.stage2.repository.StudentDetailsRepository;
import org.example.stage2.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Transactional annotations are used in this service layer for several important reasons:
 *
 * 1. Data integrity - Ensures all database operations within a method either complete
 *    successfully or roll back entirely, maintaining data consistency
 *
 * 2. Performance optimization - Using readOnly=true for query methods helps Hibernate
 *    optimize performance by disabling dirty checking and potentially using read replicas
 *
 * 3. Declarative transaction management - Allows Spring to handle transaction boundaries
 *    automatically, reducing error-prone manual transaction handling
 *
 * 4. Code consistency - Provides a uniform approach to transaction management across
 *    all service methods, making the code more maintainable and easier to understand
 *
 * 5. Future-proofing - Even for simple operations, having transaction boundaries defined
 *    prepares the code for future enhancements that might require more complex operations
 *
 * While simple repository methods have their own transaction scope, service methods
 * often combine multiple operations or contain business logic that benefits from
 * explicit transaction management.
 */


@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
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

        // Convert DTO to an entity using the mapper
        Student student = studentMapper.toEntity(studentDto);

        // Handle details relationship explicitly to ensure bidirectional integrity
        if (student.getDetails() != null) {
            StudentDetails details = student.getDetails();
            details.setStudent(student); // Set bidirectional relationship
        }

        // Save the student (will also save details via cascade)
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

        // Check if another student already uses the updated email - optimized check
        if (studentDto.getEmail() != null && !studentDto.getEmail().equals(existingStudent.getEmail())) {
            studentRepository.findByEmail(studentDto.getEmail())
                    .ifPresent(student -> {
                        if (!student.getId().equals(id)) {
                            throw new AlreadyExists("Email " + studentDto.getEmail() + " is already in use");
                        }
                    });
        }

        if (studentDto.getDetails() != null) {
            studentDto.getDetails().setStudentId(id);
        }

        // Update the existing entity from the DTO
        studentMapper.updateEntityFromDto(existingStudent, studentDto);

        // Ensure the bidirectional relationship is maintained
        if (existingStudent.getDetails() != null) {
            existingStudent.getDetails().setStudent(existingStudent);
        }

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
        // Check if a student exists
        if (!studentRepository.existsById(id)) {
            throw new NotExists("Student with id " + id + " does not exist");
        }

        studentRepository.deleteById(id);
    }
}