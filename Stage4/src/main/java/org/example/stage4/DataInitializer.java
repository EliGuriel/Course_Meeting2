package org.example.stage4;

import org.example.stage4.entity.Student;
import org.example.stage4.entity.StudentDetails;
import org.example.stage4.entity.Teacher;
import org.example.stage4.repository.StudentRepository;
import org.example.stage4.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Component
public class DataInitializer implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    @Autowired
    public DataInitializer(StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Check if there are already records in the database
        if (teacherRepository.count() == 0) {
            // Create teachers with initialized sets
            Teacher mathTeacher = new Teacher();
            mathTeacher.setName("David Cohen");
            mathTeacher.setSubject("Mathematics");
            mathTeacher.setStudents(new HashSet<>());
            
            Teacher physicsTeacher = new Teacher();
            physicsTeacher.setName("Sarah Levy");
            physicsTeacher.setSubject("Physics");
            physicsTeacher.setStudents(new HashSet<>());
            
            Teacher englishTeacher = new Teacher();
            englishTeacher.setName("Michael Berg");
            englishTeacher.setSubject("English");
            englishTeacher.setStudents(new HashSet<>());
            
            teacherRepository.save(mathTeacher);
            teacherRepository.save(physicsTeacher);
            teacherRepository.save(englishTeacher);
            
            // Create students
            Student moshe = new Student();
            moshe.setFirstName("Moshe");
            moshe.setLastName("Cohen");
            moshe.setAge(21.5);
            moshe.setEmail("moshe.cohen@example.com");
            moshe.setTeachers(new HashSet<>());
            
            StudentDetails mosheDetails = new StudentDetails();
            mosheDetails.setAddress("123 Herzl St, Tel Aviv");
            mosheDetails.setPhoneNumber("+972541234567");
            mosheDetails.setEmergencyContactName("Sarah Cohen");
            mosheDetails.setEmergencyContactPhone("+972541234568");
            mosheDetails.setStudent(moshe);
            moshe.setDetails(mosheDetails);
            
            Student yael = new Student();
            yael.setFirstName("Yael");
            yael.setLastName("Levy");
            yael.setAge(22.3);
            yael.setEmail("yael.levy@example.com");
            yael.setTeachers(new HashSet<>());
            
            StudentDetails yaelDetails = new StudentDetails();
            yaelDetails.setAddress("45 Ben Gurion Blvd, Jerusalem");
            yaelDetails.setPhoneNumber("+972521234567");
            yaelDetails.setEmergencyContactName("David Levy");
            yaelDetails.setEmergencyContactPhone("+972521234568");
            yaelDetails.setStudent(yael);
            yael.setDetails(yaelDetails);
            
            Student noam = new Student();
            noam.setFirstName("Noam");
            noam.setLastName("Mizrachi");
            noam.setAge(20.7);
            noam.setEmail("noam.mizrachi@example.com");
            noam.setTeachers(new HashSet<>());
            
            StudentDetails noamDetails = new StudentDetails();
            noamDetails.setAddress("78 Weizmann St, Haifa");
            noamDetails.setPhoneNumber("+972531234567");
            noamDetails.setEmergencyContactName("Ruth Mizrachi");
            noamDetails.setEmergencyContactPhone("+972531234568");
            noamDetails.setStudent(noam);
            noam.setDetails(noamDetails);
            
            studentRepository.save(moshe);
            studentRepository.save(yael);
            studentRepository.save(noam);
            
            // Assign students to teachers
            // Math teacher with 2 students
            mathTeacher.getStudents().add(moshe);
            mathTeacher.getStudents().add(yael);
            moshe.getTeachers().add(mathTeacher);
            yael.getTeachers().add(mathTeacher);
            
            // Physics teacher with 2 students
            physicsTeacher.getStudents().add(moshe);
            physicsTeacher.getStudents().add(noam);
            moshe.getTeachers().add(physicsTeacher);
            noam.getTeachers().add(physicsTeacher);
            
            // English teacher with 2 students
            englishTeacher.getStudents().add(yael);
            englishTeacher.getStudents().add(noam);
            yael.getTeachers().add(englishTeacher);
            noam.getTeachers().add(englishTeacher);
            
            teacherRepository.save(mathTeacher);
            teacherRepository.save(physicsTeacher);
            teacherRepository.save(englishTeacher);
            
            studentRepository.save(moshe);
            studentRepository.save(yael);
            studentRepository.save(noam);
            
            System.out.println("Data initialization completed successfully.");
        } else {
            System.out.println("Database already contains records. Skipping initialization.");
        }
    }
}