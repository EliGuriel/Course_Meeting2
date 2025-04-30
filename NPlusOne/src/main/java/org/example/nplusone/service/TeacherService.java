package org.example.nplusone.service;

import lombok.extern.slf4j.Slf4j;
import org.example.nplusone.dto.StudentDto;
import org.example.nplusone.dto.TeacherDto;
import org.example.nplusone.entity.Teacher;
import org.example.nplusone.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TeacherService {
    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    // דוגמה שתיצור בעיית N+1
    public List<TeacherDto> getAllTeachersWithN1Problem() {
        log.info("Getting all teachers with N+1 problem");
        List<Teacher> teachers = teacherRepository.findAll();

        // המרה ל-DTOs
        return teachers.stream().map(teacher -> {
            TeacherDto dto = new TeacherDto();
            dto.setId(teacher.getId());
            dto.setName(teacher.getName());
            dto.setSubject(teacher.getSubject());

            // כאן מתרחשת בעיית N+1 - שאילתה נפרדת לכל מורה
            List<StudentDto> studentDtos = teacher.getStudents().stream()
                    .map(student -> {
                        StudentDto studentDto = new StudentDto();
                        studentDto.setId(student.getId());
                        studentDto.setName(student.getFirstName() + " " + student.getLastName());

                        // עוד בעיית N+1 - שאילתה נפרדת לפרטי כל סטודנט
                        if (student.getDetails() != null) {
                            studentDto.setPhoneNumber(student.getDetails().getPhoneNumber());
                        }

                        return studentDto;
                    })
                    .collect(Collectors.toList());

            dto.setStudents(studentDtos);
            return dto;
        }).collect(Collectors.toList());
    }

    // פתרון לבעיית N+1 באמצעות JOIN FETCH
    public List<TeacherDto> getAllTeachersWithoutN1Problem() {
        log.info("Getting all teachers without N+1 problem");
        // שימוש בשאילתה שטוענת את כל הנתונים הדרושים בבת אחת
        List<Teacher> teachers = teacherRepository.findAllWithStudentsAndDetails();

        // המרה ל-DTOs אותו הקוד, אבל הפעם ללא שאילתות נוספות
        return teachers.stream().map(teacher -> {
            TeacherDto dto = new TeacherDto();
            dto.setId(teacher.getId());
            dto.setName(teacher.getName());
            dto.setSubject(teacher.getSubject());

            List<StudentDto> studentDtos = teacher.getStudents().stream()
                    .map(student -> {
                        StudentDto studentDto = new StudentDto();
                        studentDto.setId(student.getId());
                        studentDto.setName(student.getFirstName() + " " + student.getLastName());

                        if (student.getDetails() != null) {
                            studentDto.setPhoneNumber(student.getDetails().getPhoneNumber());
                        }

                        return studentDto;
                    })
                    .collect(Collectors.toList());

            dto.setStudents(studentDtos);
            return dto;
        }).collect(Collectors.toList());
    }

    // פתרון אלטרנטיבי באמצעות EntityGraph
    public List<TeacherDto> getAllTeachersWithEntityGraph() {
        log.info("Getting all teachers with EntityGraph");
        List<Teacher> teachers = teacherRepository.findAllWithGraph();

        // המרה ל-DTOs (זהה לפתרון הקודם)
        // ...
        return null; // לצורך הדוגמה
    }
}
