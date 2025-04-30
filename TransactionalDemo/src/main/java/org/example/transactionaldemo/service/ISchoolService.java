package org.example.transactionaldemo.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.example.transactionaldemo.entity.Student;
import org.example.transactionaldemo.entity.Teacher;
import org.example.transactionaldemo.exception.ResourceNotFoundException;
import org.example.transactionaldemo.exception.RollingBackException;

import java.util.List;

/**
 * Interface for school service operations.
 * This interface defines the contract for transactional operations
 * related to school management, including creating and updating
 * teachers and students.
 */
public interface ISchoolService {
    
    /**
     * Creates a teacher and a student within the same transaction.
     * 
     * Transaction behavior:
     * - If any exception occurs during the process, the entire transaction will be rolled back
     * - Both entities (student and teacher) will be saved successfully, or neither will be saved
     * 
     * @param teacher Teacher entity to be created
     * @param student Student entity to be created
     * @throws RollingBackException If the subject is "Invalid" or other validation errors occur
     */
    void createTeacherAndStudent(@Valid @NotNull Teacher teacher, @Valid @NotNull Student student);
    
    /**
     * Updates a teacher and student as a single transaction.
     * 
     * Transaction behavior:
     * - Both updates succeed, or both are rolled back
     * - Deliberate error checking for names containing "error" string
     * 
     * @param teacherId ID of the teacher to update
     * @param studentId ID of the student to update
     * @param newTeacherName New name for the teacher
     * @param newStudentName New name for the student
     * @throws ResourceNotFoundException If either entity is not found
     * @throws RollingBackException If the teacher name contains "error"
     */
    void updateTeacherAndStudent(@NotNull Long teacherId, @NotNull Long studentId,
                               @NotNull String newTeacherName, @NotNull String newStudentName);
    
    /**
     * Gets all students.
     * 
     * @return List of all students
     */
    List<Student> getAllStudents();
    
    /**
     * Gets all teachers.
     * 
     * @return List of all teachers
     */
    List<Teacher> getAllTeachers();
    
    /**
     * Gets a student by ID.
     * 
     * @param studentId ID of the student to find
     * @return The student with the specified ID
     * @throws ResourceNotFoundException If student is not found
     */
    Student getStudentById(@NotNull Long studentId);
    
    /**
     * Gets a teacher by ID.
     * 
     * @param teacherId ID of the teacher to find
     * @return The teacher with the specified ID
     * @throws ResourceNotFoundException If teacher is not found
     */
    Teacher getTeacherById(@NotNull Long teacherId);
    
    /**
     * Deletes a student by ID.
     * 
     * @param studentId ID of the student to delete
     * @throws ResourceNotFoundException If student is not found
     */
    void deleteStudent(@NotNull Long studentId);
    
    /**
     * Deletes a teacher by ID.
     * 
     * @param teacherId ID of the teacher to delete
     * @throws ResourceNotFoundException If teacher is not found
     */
    void deleteTeacher(@NotNull Long teacherId);
    
    /**
     * Gets students by teacher ID.
     * 
     * @param teacherId ID of the teacher
     * @return List of students associated with the teacher
     * @throws ResourceNotFoundException If teacher is not found
     */
    List<Student> getStudentsByTeacherId(@NotNull Long teacherId);
    
    /**
     * Processes multiple students in batch mode.
     * Each student is processed in a separate transaction.
     * 
     * @param studentIds List of student IDs to process
     * @return Number of successfully processed students
     */
    int batchProcessStudents(List<Long> studentIds);
    
    /**
     * Process a single student in its own transaction.
     * 
     * @param studentId ID of the student to process
     * @throws ResourceNotFoundException If student is not found
     */
    void processStudentInTransaction(Long studentId);
}