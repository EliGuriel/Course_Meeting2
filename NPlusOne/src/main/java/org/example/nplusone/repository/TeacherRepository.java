package org.example.nplusone.repository;

import org.example.nplusone.entity.Teacher;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    // גישה רגילה שתיצור בעיית N+1
    List<Teacher> findAll();

    // פתרון באמצעות JOIN FETCH
    @Query("SELECT t FROM Teacher t LEFT JOIN FETCH t.students")
    List<Teacher> findAllWithStudents();

    // פתרון עם טעינה עמוקה יותר
    @Query("SELECT t FROM Teacher t LEFT JOIN FETCH t.students s LEFT JOIN FETCH s.details")
    List<Teacher> findAllWithStudentsAndDetails();

    // פתרון אלטרנטיבי באמצעות EntityGraph
    @EntityGraph(attributePaths = {"students", "students.details"})
    @Query("SELECT t FROM Teacher t")
    List<Teacher> findAllWithGraph();
}
