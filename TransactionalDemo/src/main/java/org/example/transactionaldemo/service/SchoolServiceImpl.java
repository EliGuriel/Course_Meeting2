package org.example.transactionaldemo.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.example.transactionaldemo.entity.Student;
import org.example.transactionaldemo.entity.Teacher;
import org.example.transactionaldemo.exception.ResourceNotFoundException;
import org.example.transactionaldemo.exception.RollingBackException;
import org.example.transactionaldemo.repository.StudentRepository;
import org.example.transactionaldemo.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.logging.Logger;

/**
 * Implementation of school service operations.
 * This class implements the ISchoolService interface and handles
 * all transaction-related operations for school management.
 */
@Service
@Validated // Enable method parameter validation
public class SchoolServiceImpl implements ISchoolService {
    private static final Logger logger = Logger.getLogger(SchoolServiceImpl.class.getName());

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    
    // Application context for getting proxied instances when needed
    @Autowired
    private ApplicationContext context;

    @Autowired
    public SchoolServiceImpl(StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createTeacherAndStudent(@Valid @NotNull Teacher teacher, @Valid @NotNull Student student) {
        logger.info("Starting transaction to create teacher and student");
        
        // Service-level validation
        if (teacher.getSubject() != null && teacher.getSubject().equals("Invalid")) {
            logger.warning("Invalid subject detected, throwing RollingBackException");
            throw new RollingBackException("Invalid subject at service level - RollingBackException");
        }

        // Save teacher first to get ID
        logger.info("Saving teacher: " + teacher.getName());
        Teacher savedTeacher = teacherRepository.save(teacher);
        
        // Set the teacher ID in the student and save
        student.setTeacherId(savedTeacher.getId());
        logger.info("Saving student: " + student.getName() + " with teacher ID: " + savedTeacher.getId());
        Student savedStudent = studentRepository.save(student);
        
        logger.info("Successfully created teacher and student - transaction will be committed");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackOn = {Exception.class})
    public void updateTeacherAndStudent(@NotNull Long teacherId, @NotNull Long studentId,
                                        @NotNull String newTeacherName, @NotNull String newStudentName) {
        logger.info("Starting transaction to update teacher ID " + teacherId + 
                    " and student ID " + studentId);
        
        // Find and update the student
        logger.info("Finding student with ID: " + studentId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + studentId + " not found"));
        
        logger.info("Updating student name to: " + newStudentName);
        student.setName(newStudentName);
        studentRepository.save(student);

        // Find and update the teacher
        logger.info("Finding teacher with ID: " + teacherId);
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher with id " + teacherId + " not found"));
        
        logger.info("Updating teacher name to: " + newTeacherName);
        teacher.setName(newTeacherName);
        teacherRepository.save(teacher);

        // Deliberate validation check to demonstrate rollback
        if (newTeacherName != null && newTeacherName.contains("error")) {
            logger.warning("Teacher name contains 'error', throwing RollingBackException");
            throw new RollingBackException("Error updating teacher - name contains 'error'");
        }
        
        logger.info("Successfully updated teacher and student - transaction will be committed");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Student> getAllStudents() {
        logger.info("Retrieving all students");
        return studentRepository.findAll();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Teacher> getAllTeachers() {
        logger.info("Retrieving all teachers");
        return teacherRepository.findAll();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Student getStudentById(@NotNull Long studentId) {
        logger.info("Retrieving student with ID: " + studentId);
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + studentId + " not found"));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Teacher getTeacherById(@NotNull Long teacherId) {
        logger.info("Retrieving teacher with ID: " + teacherId);
        return teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher with id " + teacherId + " not found"));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteStudent(@NotNull Long studentId) {
        logger.info("Deleting student with ID: " + studentId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + studentId + " not found"));
        
        studentRepository.delete(student);
        logger.info("Student deleted successfully");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteTeacher(@NotNull Long teacherId) {
        logger.info("Deleting teacher with ID: " + teacherId);
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher with id " + teacherId + " not found"));
        
        // Check if teacher has students before deleting
        List<Student> students = studentRepository.findByTeacherId(teacherId);
        if (!students.isEmpty()) {
            logger.warning("Cannot delete teacher with students assigned");
            throw new RollingBackException("Cannot delete teacher with ID " + teacherId + 
                                         " because there are " + students.size() + " students assigned");
        }
        
        teacherRepository.delete(teacher);
        logger.info("Teacher deleted successfully");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Student> getStudentsByTeacherId(@NotNull Long teacherId) {
        logger.info("Retrieving students for teacher ID: " + teacherId);
        // Verify teacher exists
        if (!teacherRepository.existsById(teacherId)) {
            throw new ResourceNotFoundException("Teacher with id " + teacherId + " not found");
        }
        
        return studentRepository.findByTeacherId(teacherId);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int batchProcessStudents(List<Long> studentIds) {
        logger.info("Starting batch processing of " + studentIds.size() + " students");
        int successCount = 0;
        
        for (Long id : studentIds) {
            try {
                // כאן נשתמש ב-ApplicationContext במקום ב-self
                ISchoolService proxy = context.getBean(ISchoolService.class);
                proxy.processStudentInTransaction(id);
                successCount++;
            } catch (Exception e) {
                logger.warning("Error processing student ID " + id + ": " + e.getMessage());
                // Continue with next student
            }
        }
        
        logger.info("Batch processing completed. Successfully processed " + successCount + " students");
        return successCount;
    }
    
    /**
     * Process a single student in its own transaction.
     * Using a separate transaction for each student prevents issues with long-running transactions.
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void processStudentInTransaction(Long studentId) {
        logger.info("Processing student ID: " + studentId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + studentId + " not found"));
        
        // Example processing
        student.setProcessed(true);
        studentRepository.save(student);
        logger.info("Student processed successfully");
    }
    
    /**
     * Example of calling a transactional method through ApplicationContext.
     * This demonstrates the proper way to ensure transaction support for internal calls.
     */
    public void demoTransactionalMethodCall(Teacher teacher, Student student) {
        // Get proxied instance through the application context
        ISchoolService proxy = context.getBean(ISchoolService.class);
        proxy.createTeacherAndStudent(teacher, student);
    }
}