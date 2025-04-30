package org.example.transactionaldemo.repository;

import org.example.transactionaldemo.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Teacher entity operations.
 * Extends JpaRepository to inherit common data access methods.
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
    /**
     * Finds a teacher by their name.
     * This method is automatically implemented by Spring Data JPA based on the method name.
     * 
     * @param name The name of the teacher to find
     * @return The Teacher with the given name, or null if not found
     */
    Teacher findByName(String name);
}