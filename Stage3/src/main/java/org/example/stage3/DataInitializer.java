package org.example.stage3;

import org.example.stage3.entity.Student;
import org.example.stage3.entity.StudentDetails;
import org.example.stage3.entity.Teacher;
import org.example.stage3.repository.StudentRepository;
import org.example.stage3.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
            // Create teachers
            Teacher mathTeacher = new Teacher(null, "David Cohen", "Mathematics", null);
            Teacher physicsTeacher = new Teacher(null, "Sarah Levy", "Physics", null);
            Teacher englishTeacher = new Teacher(null, "Michael Berg", "English", null);
            
            teacherRepository.save(mathTeacher);
            teacherRepository.save(physicsTeacher);
            teacherRepository.save(englishTeacher);
            
            // Create and save initial student data with details and assigned teachers
            
            // Student 1 - assigned to math teacher
            Student moshe = new Student(null, "Moshe", "Cohen", 21.5, "moshe.cohen@example.com", null, null);
            StudentDetails mosheDetails = new StudentDetails(null, "123 Herzl St, Tel Aviv", "+972541234567",
                    "Sarah Cohen", "+972541234568", null);
            mosheDetails.setStudent(moshe);
            moshe.setDetails(mosheDetails);
            mathTeacher.addStudent(moshe);
            studentRepository.save(moshe);

            // Student 2 - assigned to physics teacher
            Student yael = new Student(null, "Yael", "Levy", 22.3, "yael.levy@example.com", null, null);
            StudentDetails yaelDetails = new StudentDetails(null, "45 Ben Gurion Blvd, Jerusalem", "+972521234567",
                    "David Levy", "+972521234568", null);
            yaelDetails.setStudent(yael);
            yael.setDetails(yaelDetails);
            physicsTeacher.addStudent(yael);
            studentRepository.save(yael);

            // Student 3 - assigned to English teacher
            Student noam = new Student(null, "Noam", "Mizrachi", 20.7, "noam.mizrachi@example.com", null, null);
            StudentDetails noamDetails = new StudentDetails(null, "78 Weizmann St, Haifa", "+972531234567",
                    "Ruth Mizrachi", "+972531234568", null);
            noamDetails.setStudent(noam);
            noam.setDetails(noamDetails);
            englishTeacher.addStudent(noam);
            studentRepository.save(noam);

            // Student 4 - no teacher assigned
            Student tamar = new Student(null, "Tamar", "Goldberg", 23.1, "tamar.goldberg@example.com", null, null);
            studentRepository.save(tamar);

            // Student 5 - no teacher assigned
            Student amit = new Student(null, "Amit", "Dahan", 22.8, "amit.dahan@example.com", null, null);
            studentRepository.save(amit);

            // Save teachers again to make sure all relationships are persisted
            teacherRepository.save(mathTeacher);
            teacherRepository.save(physicsTeacher);
            teacherRepository.save(englishTeacher);

            System.out.println("Data initialization completed for stage3.");
        } else {
            System.out.println("Database already contains records. Skipping initialization.");
        }
    }
}