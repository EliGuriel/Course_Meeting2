package org.example.nplusone.repository;

import org.example.nplusone.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // כאן ניתן להוסיף מתודות מותאמות אישית אם צריך
}