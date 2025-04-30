package org.example.stage4.repository;

import org.example.stage4.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Find a student by email
     */
    Optional<Student> findByEmail(String email);
    
    /**
     * Find all students with their details and teachers
     */
    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.details LEFT JOIN FETCH s.teachers")
    List<Student> findAllWithDetailsAndTeachers();
    
    /**
     * Find students who don't have any teachers assigned
     */
    @Query("SELECT s FROM Student s WHERE s.teachers IS EMPTY")
    List<Student> findStudentsWithoutTeachers();
}