package org.example.stage2;

import org.example.stage2.entity.Student;
import org.example.stage2.entity.StudentDetails;
import org.example.stage2.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final StudentRepository studentRepository;

    @Autowired
    public DataInitializer(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void run(String... args) {
        // Check if there are already records in the database
        if (studentRepository.count() == 0) {
            // Create and save initial student data with details

            // Student 1
            Student moshe = new Student(null, "Moshe", "Cohen", 21.5, "moshe.cohen@example.com", null);
            StudentDetails mosheDetails = new StudentDetails(null, "123 Herzl St, Tel Aviv", "+972541234567",
                    "Sarah Cohen", "+972541234568", null);
            mosheDetails.setStudent(moshe);
            moshe.setDetails(mosheDetails);
            studentRepository.save(moshe);

            // Student 2
            Student yael = new Student(null, "Yael", "Levy", 22.3, "yael.levy@example.com", null);
            StudentDetails yaelDetails = new StudentDetails(null, "45 Ben Gurion Blvd, Jerusalem", "+972521234567",
                    "David Levy", "+972521234568", null);
            yaelDetails.setStudent(yael);
            yael.setDetails(yaelDetails);
            studentRepository.save(yael);

            // Student 3
            Student noam = new Student(null, "Noam", "Mizrachi", 20.7, "noam.mizrachi@example.com", null);
            StudentDetails noamDetails = new StudentDetails(null, "78 Weizmann St, Haifa", "+972531234567",
                    "Ruth Mizrachi", "+972531234568", null);
            noamDetails.setStudent(noam);
            noam.setDetails(noamDetails);
            studentRepository.save(noam);

            // Student 4 - without details
            Student tamar = new Student(null, "Tamar", "Goldberg", 23.1, "tamar.goldberg@example.com", null);
            studentRepository.save(tamar);

            // Student 5 - without details
            Student amit = new Student(null, "Amit", "Dahan", 22.8, "amit.dahan@example.com", null);
            studentRepository.save(amit);

            System.out.println("Data initialization completed. Created 5 student records (3 with details).");
        } else {
            System.out.println("Database already contains records. Skipping initialization.");
        }
    }
}