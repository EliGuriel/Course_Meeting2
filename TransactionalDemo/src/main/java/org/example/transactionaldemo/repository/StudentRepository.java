package org.example.transactionaldemo.repository;

import org.example.transactionaldemo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Student entity operations.
 * Extends JpaRepository to inherit common data access methods.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    /**
     * Custom query to retrieve only the IDs of all students.
     * This is more efficient than fetching entire entities when only IDs are needed.
     * Used for batch processing to avoid loading all data at once.
     * 
     * @return List of student IDs
     */
    @Query("SELECT s.id FROM Student s")
    List<Long> findAllIds();
    
    /**
     * Finds a student by their name.
     * This method is automatically implemented by Spring Data JPA based on method name.
     * 
     * @param name The name of the student to find
     * @return The Student with the given name, or null if not found
     */
    Student findByName(String name);
    
    /**
     * Finds students by email.
     * This method is automatically implemented by Spring Data JPA based on method name.
     * 
     * @param email The email to search for
     * @return The Student with the given email, or null if not found
     */
    Student findByEmail(String email);
    
    /**
     * Finds all students associated with a specific teacher.
     * 
     * @param teacherId The ID of the teacher
     * @return List of students associated with the teacher
     */
    @Query("SELECT s FROM Student s WHERE s.teacherId = :teacherId")
    List<Student> findByTeacherId(@Param("teacherId") Long teacherId);
    
    /**
     * Updates the 'processed' flag for a student.
     * 
     * @param studentId The ID of the student to update
     * @param processed The new processed status
     * @return Number of affected rows
     */
    @Modifying
    @Query("UPDATE Student s SET s.processed = :processed WHERE s.id = :studentId")
    int updateProcessedStatus(@Param("studentId") Long studentId, @Param("processed") boolean processed);
    
    /**
     * Counts students with a specific email domain.
     * 
     * @param domain The email domain to search for (e.g., "example.com")
     * @return Number of students with the specified email domain
     */
    @Query("SELECT COUNT(s) FROM Student s WHERE s.email LIKE %:domain")
    long countByEmailDomain(@Param("domain") String domain);
}