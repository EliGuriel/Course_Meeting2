package org.example.stage1.service;

import org.example.stage1.dto.StudentDto;
import org.example.stage1.entity.Student;
import org.example.stage1.exception.AlreadyExists;
import org.example.stage1.exception.NotExists;
import org.example.stage1.exception.StudentIdAndIdMismatch;
import org.example.stage1.mapper.StudentMapper;
import org.example.stage1.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/*
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

    /**
     * Get all students from the system as DTOs
     * @return List of all students as DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }



    /**
     * Get student by ID as DTO
     * @param id The student ID to retrieve
     * @return The found student as DTO
     * @throws NotExists If a student doesn't exist
     */
    @Override
    @Transactional(readOnly = true)
    public StudentDto getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NotExists("Student with id " + id + " does not exist"));
                
        return studentMapper.toDto(student);
    }


    /**
     * Add a new student
     * @param studentDto Student data to add (as DTO)
     * @return The added student as DTO
     * @throws AlreadyExists If a student with the same ID already exists
     */
    @Override
    @Transactional
    public StudentDto addStudent(StudentDto studentDto) {
        // Check if a student with the same email already exists
        if (studentRepository.findByEmail(studentDto.getEmail()).isPresent()) {
            throw new AlreadyExists("Student with email " + studentDto.getEmail() + " already exists");
        }

        // Convert DTO to an entity using the mapper
        Student student = studentMapper.toEntity(studentDto);
        
        // Save entity and convert back to DTO
        Student added = studentRepository.save(student);
        return studentMapper.toDto(added);
    }



    /**
     * Update an existing student
     * @param studentDto Updated student data (as DTO)
     * @param id The ID from the path parameter
     * @return The updated student as DTO
     * @throws NotExists If a student doesn't exist
     * @throws StudentIdAndIdMismatch If ID in a path doesn't match student ID
     */

    @Override
    @Transactional
    public StudentDto updateStudent(StudentDto studentDto, Long id) {
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

        // Save the updated entity and convert back to DTO
        Student updated = studentRepository.save(existingStudent);
        return studentMapper.toDto(updated);
    }



    /**
     * Delete a student by ID
     * @param id Student ID to delete
     * @throws NotExists If a student doesn't exist
     */
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