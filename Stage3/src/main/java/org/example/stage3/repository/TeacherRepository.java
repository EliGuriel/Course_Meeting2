package org.example.stage3.repository;

import org.example.stage3.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
    // Find a teacher with all their students loaded
    @Query("SELECT t FROM Teacher t LEFT JOIN FETCH t.students WHERE t.id = :id")
    Optional<Teacher> findByIdWithStudents(@Param("id") Long id);
    
    // Find teachers by subject
    List<Teacher> findBySubject(String subject);
}